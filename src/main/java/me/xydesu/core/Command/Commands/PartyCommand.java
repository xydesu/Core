package me.xydesu.core.Command.Commands;

import me.xydesu.core.Command.Command;
import me.xydesu.core.Player.Party.PartyManager;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * /party <invite|accept|kick|leave|disband|list>
 */
public class PartyCommand extends Command {

    @Override
    public String getCommand() {
        return "party";
    }

    @Override
    public void onExecute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("§c此指令只能由玩家使用。");
            return;
        }

        if (args.length == 0) {
            sendHelp(player);
            return;
        }

        switch (args[0].toLowerCase()) {
            case "invite" -> {
                if (args.length < 2) { player.sendMessage("§c用法: /party invite <玩家名稱>"); return; }
                Player target = Bukkit.getPlayer(args[1]);
                if (target == null) { player.sendMessage("§c找不到玩家 §e" + args[1]); return; }
                PartyManager.invite(player, target);
            }
            case "accept" -> PartyManager.accept(player);
            case "kick" -> {
                if (args.length < 2) { player.sendMessage("§c用法: /party kick <玩家名稱>"); return; }
                Player target = Bukkit.getPlayer(args[1]);
                if (target == null) { player.sendMessage("§c找不到玩家 §e" + args[1]); return; }
                PartyManager.kick(player, target);
            }
            case "leave" -> PartyManager.leave(player);
            case "disband" -> PartyManager.disband(player);
            case "list" -> {
                me.xydesu.core.Player.Party.Party party = PartyManager.getParty(player.getUniqueId());
                if (party == null) { player.sendMessage("§c你目前不在隊伍中。"); return; }
                player.sendMessage("§6隊伍成員 (" + party.size() + ")：");
                for (java.util.UUID uuid : party.getMembers()) {
                    String name = Bukkit.getOfflinePlayer(uuid).getName();
                    boolean isLeader = uuid.equals(party.getLeader());
                    boolean online = Bukkit.getPlayer(uuid) != null;
                    player.sendMessage("§7 - §e" + (name != null ? name : uuid) +
                            (isLeader ? " §6[隊長]" : "") +
                            (online ? "" : " §8(離線)"));
                }
            }
            default -> sendHelp(player);
        }
    }

    private void sendHelp(Player player) {
        player.sendMessage("§6隊伍指令：");
        player.sendMessage("§e /party invite <玩家> §7- 邀請玩家加入隊伍");
        player.sendMessage("§e /party accept §7- 接受隊伍邀請");
        player.sendMessage("§e /party kick <玩家> §7- 踢出隊員 (隊長限定)");
        player.sendMessage("§e /party leave §7- 離開隊伍");
        player.sendMessage("§e /party disband §7- 解散隊伍 (隊長限定)");
        player.sendMessage("§e /party list §7- 查看隊伍成員");
    }

    @Override
    public String getDescription() {
        return "Party management";
    }

    @Override
    public String getUsage() {
        return "/party <invite|accept|kick|leave|disband|list>";
    }

    @Override
    protected List<String> getTabCompletions(CommandSender sender, org.bukkit.command.Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            completions.addAll(List.of("invite", "accept", "kick", "leave", "disband", "list"));
        } else if (args.length == 2 && (args[0].equalsIgnoreCase("invite") || args[0].equalsIgnoreCase("kick"))) {
            return null; // player names
        }
        return completions;
    }
}
