package at.jojokobi.blockykingdom.players.skills;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.entity.Entity;
import org.bukkit.Location;

import at.jojokobi.blockykingdom.BlockyKingdomPlugin;
import at.jojokobi.blockykingdom.players.CharacterStats;
import at.jojokobi.blockykingdom.players.StatHandler;
import org.bukkit.event.player.PlayerToggleSneakEvent;

import java.util.*;

public class ClimbingSkill extends Skill {

	private static final String IDENTIFIER = "climbing";
	private Map<UUID, Long> timestamps = new HashMap<>();
	private int COOLDOWN = 50000;
	private Set<UUID> climbing = new HashSet<>();
	public ClimbingSkill() {
		super(8, 20, "Climbing");
	}

/*
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		CharacterStats stats = StatHandler.getInstance().getStats(player).getCharacterStats();
		int level = stats.getSkillLevel(this);
		if (level > 0 && player.isSneaking()
				&& (player.getLocation().add(1, 0, 0).getBlock().getType().isSolid()
				|| player.getLocation().add(-1, 0, 0).getBlock().getType().isSolid()
				|| player.getLocation().add(0, 0, 1).getBlock().getType().isSolid()
				|| player.getLocation().add(0, 0, -1).getBlock().getType().isSolid())) {
			player.setVelocity(player.getVelocity().setY(player.getLocation().getPitch()/-90 * 0.1 * level));
			player.setFallDistance(0);
		}
	}
*/


	public static int getDistance(Entity e){
		Location loc = e.getLocation().clone();
		double y = loc.getBlockY();
		int distance = 0;
		for (double i = y; i >= 0; i--){
			loc.setY(i);
			if(loc.getBlock().getType().isSolid())break;
			distance++;
		}
		return distance;
	}

	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		CharacterStats stats = StatHandler.getInstance().getStats(player).getCharacterStats();
		int level = stats.getSkillLevel(this);

		if (level > 0 && player.isSneaking()
				&& (player.getLocation().add(1, 0, 0).getBlock().getType().isSolid()
				|| player.getLocation().add(-1, 0, 0).getBlock().getType().isSolid()
				|| player.getLocation().add(0, 0, 1).getBlock().getType().isSolid()
				|| player.getLocation().add(0, 0, -1).getBlock().getType().isSolid())) {
			if (!climbing.contains(event.getPlayer().getUniqueId()) && player.getLocation().add(0, -1, 0).getBlock().getType().isSolid()){
				timestamps.put(event.getPlayer().getUniqueId(), System.currentTimeMillis());
				climbing.add(event.getPlayer().getUniqueId());
				player.setVelocity(player.getVelocity().setY(player.getLocation().getPitch()/-90 * 0.1* (1+level/3)));
				player.setFallDistance(0);
			}
			else if (climbing.contains(event.getPlayer().getUniqueId()) && player.getLocation().add(0, -1, 0).getBlock().getType().isSolid()){
				timestamps.remove(event.getPlayer().getUniqueId());
				climbing.remove(event.getPlayer().getUniqueId());
				timestamps.put(event.getPlayer().getUniqueId(), System.currentTimeMillis());
				climbing.add(event.getPlayer().getUniqueId());
				player.setVelocity(player.getVelocity().setY(player.getLocation().getPitch()/-90 * 0.1* (1+level/3)));
				player.setFallDistance(0);
			}
			else if (climbing.contains(event.getPlayer().getUniqueId()) && timestamps.get(event.getPlayer().getUniqueId())!= null && timestamps.get(event.getPlayer().getUniqueId()) + level * 300L + 1000L - System.currentTimeMillis() > 0){
				player.setVelocity(player.getVelocity().setY(player.getLocation().getPitch()/-90 * 0.1* (1+level/3)));
				player.setFallDistance(0);
			}
			else if (climbing.contains(event.getPlayer().getUniqueId()) && timestamps.get(event.getPlayer().getUniqueId())!= null && timestamps.get(event.getPlayer().getUniqueId()) + level * 300L + 1000L - System.currentTimeMillis() <= 0){
				timestamps.remove(event.getPlayer().getUniqueId());
				climbing.remove(event.getPlayer().getUniqueId());
				player.setFallDistance(getDistance(player));
			}



		}

	}

	@Override
	public boolean canLearn(CharacterStats character) {
		return super.canLearn(character);
	}

	@Override
	public String getIdentifier() {
		return IDENTIFIER;
	}

	@Override
	public String getNamespace() {
		return BlockyKingdomPlugin.BLOCKY_KINGDOM_NAMESPACE;
	}

	@Override
	public Material getMaterial() {
		return Material.LADDER;
	}
	
	@Override
	public String getDescription() {
		return "By looking up/down while sneaking torwards a wall you can climb the wall Duration and speed of climbing depends on level";
	}

}
