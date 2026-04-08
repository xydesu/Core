package me.xydesu.core.Player.Party;

import java.util.*;

/**
 * Represents a player party.  The leader is always a member of the party.
 */
public class Party {

    private final UUID leader;
    private final Set<UUID> members = new LinkedHashSet<>();

    public Party(UUID leader) {
        this.leader = leader;
        members.add(leader);
    }

    public UUID getLeader() { return leader; }

    public Set<UUID> getMembers() { return Collections.unmodifiableSet(members); }

    public boolean isMember(UUID uuid) { return members.contains(uuid); }

    public void addMember(UUID uuid) { members.add(uuid); }

    public void removeMember(UUID uuid) { members.remove(uuid); }

    public int size() { return members.size(); }
}
