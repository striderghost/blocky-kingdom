package at.jojokobi.blockykingdom.entities;

import java.util.HashMap;
import java.util.Map;

//import com.ticxo.modelengine.api.model.ModeledEntity;
//import com.ticxo.modelengine.api.model.ActiveModel;
//import com.ticxo.modelengine.api.ModelEngineAPI;
import at.jojokobi.mcutil.NamespacedEntry;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;
import org.spigotmc.event.entity.EntityDismountEvent;

import at.jojokobi.blockykingdom.BlockyKingdomPlugin;
import at.jojokobi.mcutil.entity.Attacker;
import at.jojokobi.mcutil.entity.CustomEntity;
import at.jojokobi.mcutil.entity.EntityHandler;
import at.jojokobi.mcutil.entity.EntityMapData;
import at.jojokobi.mcutil.entity.HealthComponent;
import at.jojokobi.mcutil.entity.LootComponent;
import at.jojokobi.mcutil.entity.NMSEntityUtil;
import at.jojokobi.mcutil.entity.RealHealthAccessor;
import at.jojokobi.mcutil.entity.ai.AttackTask;
import at.jojokobi.mcutil.loot.LootInventory;
import at.jojokobi.mcutil.loot.LootItem;

public class Ghost extends CustomEntity<Skeleton> implements Attacker{
	//public ModeledEntity ghostentity;
	//public ActiveModel ghostmodel;
	public static final NamespacedEntry GHOST_SPAWN_KEY = new NamespacedEntry(
			BlockyKingdomPlugin.BLOCKY_KINGDOM_NAMESPACE, "ghost");

	public Ghost(Location place, EntityHandler handler) {
		super(place, handler, GhostType.getInstance());
		setDespawnTicks(5000);
		
		LootInventory loot = new LootInventory();
		loot.addItem(new LootItem(0.25, new ItemStack(Material.BONE), 1, 2));
		loot.addItem(new LootItem(0.01, new ItemStack(Material.CHAINMAIL_HELMET), 1, 1));
		loot.addItem(new LootItem(0.01, new ItemStack(Material.CHAINMAIL_CHESTPLATE), 1, 1));
		loot.addItem(new LootItem(0.01, new ItemStack(Material.CHAINMAIL_LEGGINGS), 1, 1));
		loot.addItem(new LootItem(0.01, new ItemStack(Material.CHAINMAIL_BOOTS), 1, 1));
		loot.addItem(new LootItem(0.01, new ItemStack(Material.ENDER_PEARL), 1, 1));
		
		addComponent(new HealthComponent(new RealHealthAccessor()));
		addComponent(new LootComponent(loot, 5));
		addEntityTask(new ThrowDownTask());
		addEntityTask(new AttackTask(Player.class));
//		setAi(GhostThrowAI.getInstance());
	}

	@Override
	protected Skeleton createEntity(Location place) {
		Skeleton entity = place.getWorld().spawn(place, Skeleton.class);
		NMSEntityUtil.clearGoals(entity);
		entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(20.0);
		entity.setHealth(entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
		entity.setRemoveWhenFarAway(true);

		entity.getEquipment().setHelmet(new ItemStack(Material.CHAINMAIL_HELMET));
		entity.getEquipment().setChestplate(new ItemStack(Material.CHAINMAIL_CHESTPLATE));
		entity.getEquipment().setLeggings(new ItemStack(Material.CHAINMAIL_LEGGINGS));
		entity.getEquipment().setItemInMainHand(null);
		entity.getEquipment().setItemInMainHandDropChance(0.1f);
		entity.getEquipment().setItemInOffHandDropChance(0.1f);
		//entity.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 1000000, 1, true, false));
		entity.setLootTable(null);
		//ghostentity = ModelEngineAPI.api.getModelManager().createModeledEntity(entity);
		//ghostmodel = ModelEngineAPI.api.getModelManager().createActiveModel("darklurker");
		//ghostentity.addActiveModel(ghostmodel);
		//ghostentity.setInvisible(true);
		//ghostentity.getNametagHandler().setCustomName("tag_name", "Ghost");
		//ghostentity.getNametagHandler().setCustomNameVisibility("tag_name", true);

		return entity;
	}
	
	@Override
	public void loop() {
		super.loop();
	}
	
	@Override
	protected void onGetDismounted(EntityDismountEvent event) {
		super.onGetDismounted(event);
		event.getEntity().setVelocity(event.getEntity().getVelocity().add(new Vector(0, 0.75, 0)));
	}
	
	@Override
	protected void onDamage(EntityDamageEvent event) {
		if (event.getCause() == DamageCause.FIRE || event.getCause() == DamageCause.FIRE_TICK) {
			event.setDamage(20);
		}
		super.onDamage(event);
	}
	
	@Override
	public void delete() {
		getEntity().eject();
		super.delete();
	}
	
	@Override
	public void attack(Damageable entity) {
		//ModelEngineAPI.api.getModelManager().getModeledEntity(getEntity().getUniqueId()).getActiveModel("darklurker").addState("darklurkerfly",1,1,1.0);
		getEntity().addPassenger(entity);
	}

	@Override
	public int getAttackDelay() {
		return 14;
	}
	
	@Override
	protected double getFlySpeed() {
		return 0.7;
	}
	
	@Override
	protected boolean canFly() {
		return true;
	}

	@Override
	protected void loadData(EntityMapData data) {
		
	}

	@Override
	protected EntityMapData saveData() {
		return new EntityMapData(new HashMap<>());
	}
	
	public static Ghost deserialize (Map<String, Object> map) {
		Ghost entity = new Ghost(null, null);
		entity.load(map);
		return entity;
	}
	
	@Override
	public Class<? extends JavaPlugin> getPlugin() {
		return BlockyKingdomPlugin.class;
	}
	
	@Override
	public double getAttackRange() {
		return 1.5;
	}
	
}
