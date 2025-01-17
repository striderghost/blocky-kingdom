package at.jojokobi.blockykingdom.entities;

import java.util.HashMap;
import java.util.Random;
import java.lang.annotation.Target;
import at.jojokobi.mcutil.entity.*;
import at.jojokobi.mcutil.locatables.EntityLocatable;
import at.jojokobi.mcutil.locatables.Locatable;
import org.bukkit.entity.LivingEntity;
//import com.ticxo.modelengine.api.generator.blueprint.ModelBlueprint;
import org.bukkit.entity.Entity;
import org.bukkit.Bukkit;
import org.bukkit.Color;
//import com.ticxo.modelengine.api.nms.DefaultRangeManager;
import org.bukkit.entity.Player;
import java.util.Iterator;
import java.util.Set;
//import com.ticxo.modelengine.api.ModelEngineAPI;
//import com.ticxo.modelengine.api.model.ModeledEntity;
//import com.ticxo.modelengine.api.model.ActiveModel;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.*;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.entity.EntityTransformEvent;
import org.bukkit.event.entity.EntityTransformEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import at.jojokobi.blockykingdom.BlockyKingdomPlugin;
import at.jojokobi.blockykingdom.items.GoblinFang;
import at.jojokobi.blockykingdom.items.GoblinKnife;
import at.jojokobi.blockykingdom.items.GoblinSkin;
import at.jojokobi.mcutil.NamespacedEntry;
import at.jojokobi.mcutil.entity.ai.ApproachEntityTask;
import at.jojokobi.mcutil.entity.ai.AttackTask;
import at.jojokobi.mcutil.entity.ai.RandomTask;
import at.jojokobi.mcutil.item.ItemHandler;
import at.jojokobi.mcutil.loot.LootInventory;
import at.jojokobi.mcutil.loot.LootItem;
import org.bukkit.event.EventHandler;
import at.jojokobi.mcutil.entity.HealthComponent;
import at.jojokobi.mcutil.entity.LootComponent;
import at.jojokobi.mcutil.entity.NMSEntityUtil;
import at.jojokobi.mcutil.entity.RealHealthAccessor;
import at.jojokobi.mcutil.entity.ai.AttackTask;
import at.jojokobi.mcutil.entity.ai.RandomTask;
import at.jojokobi.mcutil.item.ItemHandler;
import at.jojokobi.mcutil.loot.LootInventory;
import at.jojokobi.mcutil.loot.LootItem;

public class EliteGoblin extends CustomEntity<Zombie> implements Attacker{
	
	public static final NamespacedEntry ELITE_GOBLIN_SPAWN_KEY = new NamespacedEntry(BlockyKingdomPlugin.BLOCKY_KINGDOM_NAMESPACE, "elite_goblin");
	//public ModeledEntity elitegoblinentity;
	//public ActiveModel elitegoblinmodel;

	public EliteGoblin(Location place, EntityHandler handler) {
		super(place, handler, null);
		setDespawnTicks(5000);
		
		LootInventory loot = new LootInventory ();
		loot.addItem(new LootItem(0.1, ItemHandler.getItemStack(GoblinSkin.class), 1, 2));
		loot.addItem(new LootItem(0.05, ItemHandler.getItemStack(GoblinFang.class), 1, 1));
		
		addComponent(new LootComponent(loot, 10));
		addComponent(new HealthComponent(new RealHealthAccessor()));

		addEntityTask(new AttackTask(Player.class));
		addEntityTask(new RandomTask());
	}

	@Override
	public Class<? extends JavaPlugin> getPlugin() {
		return BlockyKingdomPlugin.class;
	}

	@Override
	protected Zombie createEntity(Location place) {
		Zombie entity = place.getWorld().spawn(place, Zombie.class);
		
		NMSEntityUtil.clearGoals(entity);
		
		entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(80.0);
		entity.setHealth(entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
		entity.getAttribute(Attribute.GENERIC_ARMOR).setBaseValue(20.0);
		entity.setRemoveWhenFarAway(isSave());
		
		ItemStack helmet = new ItemStack(Material.IRON_HOE);
		ItemMeta meta = helmet.getItemMeta();
		meta.setCustomModelData(4);
		meta.setUnbreakable(true);
		helmet.setItemMeta(meta);
		entity.getEquipment().setHelmet(helmet);
		entity.getEquipment().setHelmetDropChance(0);
		entity.getEquipment().setItemInMainHandDropChance(1);
		ItemStack knife = ItemHandler.getItemStack(GoblinKnife.class);
		ItemHandler.getCustomItem(GoblinKnife.class).setDurability(knife, new Random().nextInt((200 - 20) + 1) + 20);
		entity.getEquipment().setItemInMainHand(knife);
		entity.getEquipment().setItemInMainHandDropChance(0.1f);
		entity.getEquipment().setItemInOffHandDropChance(0.1f);

		entity.setLootTable(null);
		//entity.setCustomName("EliteGoblin");
		//entity.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 1000000, 1, true, false));
		//elitegoblinentity = ModelEngineAPI.api.getModelManager().createModeledEntity(entity);
		//elitegoblinmodel = ModelEngineAPI.api.getModelManager().createActiveModel("kur");
		//elitegoblinentity.addActiveModel(elitegoblinmodel);
		//elitegoblinentity.setInvisible(true);
		//elitegoblibossentity.getNametagHandler().setCustomName("tag_name", "Elite Goblin");
		//elitegoblibossentity.getNametagHandler().setCustomNameVisibility("tag_name", true);
		
		return entity;
	}
	
	@Override
	public void setSave(boolean save) {
		super.setSave(save);
		if (getEntity() != null) {
			getEntity().setRemoveWhenFarAway(save);
		}
	}

	@Override
	protected void loadData(EntityMapData data) {
		
	}

	@Override
	protected EntityMapData saveData() {
		return new EntityMapData(new HashMap<> ());
	}

	@Override
	public void attack(Damageable entity) {

		//ModelEngineAPI.api.getModelManager().getModeledEntity(getEntity().getUniqueId()).getActiveModel("kur").addState("attack3",1,1,1.0);
		entity.damage(5, getEntity());
		//entity.damage(5);
		if (getEntity().getEquipment().getItemInMainHand().getType() == Material.AIR && entity instanceof LivingEntity && Math.random() < 0.05) {
			getEntity().getEquipment().setItemInMainHand(((LivingEntity) entity).getEquipment().getItemInMainHand().clone());
			getEntity().getEquipment().setItemInMainHandDropChance(1);
			((LivingEntity) entity).getEquipment().setItemInMainHand(null);
			//ModelEngineAPI.api.getModelManager().getModeledEntity(getEntity().getUniqueId()).getActiveModel("kur").addState("spin_attack",1,1,1.0);
			//ModelEngineAPI.api.getModelManager().getModeledEntity(getEntity().getUniqueId()).getActiveModel("goblin_engineer").addState("attack2",1,1,1.0);
		}
	}

	@Override
	public int getAttackDelay() {
		return 12;
	}
	
	@Override
	protected double getSprintSpeed() {
		return 0.4;
	}

	@Override
	protected boolean canSwim() {
		return false;
	}

	@Override
	protected void onTransform(EntityTransformEvent event) {
		super.onTransform(event);
		event.setCancelled(true);
	}
	
	@Override
	public double getAttackRange() {
		return 1.5;
	}
	
}
