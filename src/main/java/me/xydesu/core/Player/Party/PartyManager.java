package me.xydesu.core.Player.Party;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;

/**
 * Manages all active {@link Party} instances and pending invitations.
 */
public class PartyManager {

    // UUID -> Party (only contains entries for players who are in a party)
    private static final Map<UUID, Party> playerParties = new HashMap<>();
    // Invitee UUID -> Inviter UUID (pending invites)
    private static final Map<UUID, UUID> pendingInvites = new HashMap<>();

    /**
     * Returns the {@link Party} the given player belongs to, or {@code null}.
     */
    public static Party getParty(UUID uuid) {
        return playerParties.get(uuid);
    }

    /**
     * Returns {@code true} if two players are in the same party.
     */
    public static boolean areInSameParty(UUID a, UUID b) {
        Party party = playerParties.get(a);
        return party != null && party.isMember(b);
    }

    /**
     * Sends a party invitation from {@code inviter} to {@code invitee}.
     */
    public static void invite(Player inviter, Player invitee) {
        if (invitee == null || !invitee.isOnline()) {
            inviter.sendMessage("§c該玩家不在線上。");
            return;
        }
        if (areInSameParty(inviter.getUniqueId(), invitee.getUniqueId())) {
            inviter.sendMessage("§c該玩家已經是你的隊友。");
            return;
        }
        if (playerParties.containsKey(invitee.getUniqueId())) {
            inviter.sendMessage("§c該玩家已經在其他隊伍中。");
            return;
        }
        pendingInvites.put(invitee.getUniqueId(), inviter.getUniqueId());
        inviter.sendMessage("§a已向 §e" + invitee.getName() + " §a發送隊伍邀請。");
        invitee.sendMessage("§e" + inviter.getName() + " §a邀請你加入隊伍！輸入 §e/party accept §a以接受。");
    }

    /**
     * Accepts the pending invitation for {@code invitee}.
     */
    public static void accept(Player invitee) {
        UUID inviterUUID = pendingInvites.remove(invitee.getUniqueId());
        if (inviterUUID == null) {
            invitee.sendMessage("§c你目前沒有待處理的隊伍邀請。");
            return;
        }

        Player inviter = Bukkit.getPlayer(inviterUUID);
        Party party = playerParties.get(inviterUUID);

        if (party == null) {
            // Inviter has no party yet – create one with the inviter as leader
            party = new Party(inviterUUID);
            playerParties.put(inviterUUID, party);
        }

        party.addMember(invitee.getUniqueId());
        playerParties.put(invitee.getUniqueId(), party);

        String inviterName = inviter != null ? inviter.getName() : "隊長";
        invitee.sendMessage("§a你已加入 §e" + inviterName + " §a的隊伍！");
        broadcastToParty(party, "§e" + invitee.getName() + " §a加入了隊伍！", null);
    }

    /**
     * Kicks {@code target} from the party.  Only the leader may kick members.
     */
    public static void kick(Player leader, Player target) {
        Party party = playerParties.get(leader.getUniqueId());
        if (party == null) {
            leader.sendMessage("§c你目前不在隊伍中。");
            return;
        }
        if (!party.getLeader().equals(leader.getUniqueId())) {
            leader.sendMessage("§c只有隊長才能踢出成員。");
            return;
        }
        if (!party.isMember(target.getUniqueId())) {
            leader.sendMessage("§c該玩家不在你的隊伍中。");
            return;
        }
        if (target.getUniqueId().equals(leader.getUniqueId())) {
            leader.sendMessage("§c你不能踢出自己。請使用 /party disband 解散隊伍。");
            return;
        }
        removeFromParty(target.getUniqueId(), party);
        target.sendMessage("§c你已被移出隊伍。");
        broadcastToParty(party, "§e" + target.getName() + " §c已被移出隊伍。", null);
    }

    /**
     * Disbands the party.  Only the leader may disband.
     */
    public static void disband(Player leader) {
        Party party = playerParties.get(leader.getUniqueId());
        if (party == null) {
            leader.sendMessage("§c你目前不在隊伍中。");
            return;
        }
        if (!party.getLeader().equals(leader.getUniqueId())) {
            leader.sendMessage("§c只有隊長才能解散隊伍。");
            return;
        }
        broadcastToParty(party, "§c隊伍已被解散。", null);
        for (UUID member : new ArrayList<>(party.getMembers())) {
            playerParties.remove(member);
        }
    }

    /**
     * Makes {@code player} leave their current party.
     */
    public static void leave(Player player) {
        Party party = playerParties.get(player.getUniqueId());
        if (party == null) {
            player.sendMessage("§c你目前不在隊伍中。");
            return;
        }
        if (party.getLeader().equals(player.getUniqueId())) {
            player.sendMessage("§c身為隊長，請使用 /party disband 解散隊伍，或先轉讓隊長。");
            return;
        }
        removeFromParty(player.getUniqueId(), party);
        player.sendMessage("§a你已離開隊伍。");
        broadcastToParty(party, "§e" + player.getName() + " §a離開了隊伍。", null);
    }

    /**
     * Cleans up party state when a player disconnects.
     */
    public static void handleDisconnect(UUID uuid) {
        pendingInvites.remove(uuid);
        Party party = playerParties.get(uuid);
        if (party == null) return;

        if (party.getLeader().equals(uuid)) {
            // Leader left – disband
            broadcastToParty(party, "§c隊長已離線，隊伍解散。", uuid);
            for (UUID member : new ArrayList<>(party.getMembers())) {
                playerParties.remove(member);
            }
        } else {
            removeFromParty(uuid, party);
            broadcastToParty(party, "§e" + Bukkit.getOfflinePlayer(uuid).getName() + " §a已離線並離開了隊伍。", null);
        }
    }

    // ── Helpers ──────────────────────────────────────────────────────────────

    private static void removeFromParty(UUID uuid, Party party) {
        party.removeMember(uuid);
        playerParties.remove(uuid);
        if (party.size() == 1) {
            // Only one member left – auto-disband
            UUID remaining = party.getMembers().iterator().next();
            playerParties.remove(remaining);
            Player remainingPlayer = Bukkit.getPlayer(remaining);
            if (remainingPlayer != null) {
                remainingPlayer.sendMessage("§c你的隊友全部離開，隊伍已解散。");
            }
        }
    }

    private static void broadcastToParty(Party party, String message, UUID exclude) {
        for (UUID member : party.getMembers()) {
            if (exclude != null && exclude.equals(member)) continue;
            Player p = Bukkit.getPlayer(member);
            if (p != null) p.sendMessage(message);
        }
    }
}
