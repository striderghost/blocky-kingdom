package at.jojokobi.blockykingdom.entities;

import java.lang.annotation.Target;
import java.util.HashMap;

import at.jojokobi.blockykingdom.*;
import at.jojokobi.mcutil.entity.*;
import at.jojokobi.mcutil.locatables.EntityLocatable;
import at.jojokobi.mcutil.locatables.Locatable;
//import com.ticxo.modelengine.ModelEngine;
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
import org.bukkit.event.entity.EntityTransformEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import net.minecraft.world.entity.monster.Spider;
import at.jojokobi.blockykingdom.BlockyKingdomPlugin;
import at.jojokobi.blockykingdom.items.GoblinFang;
import at.jojokobi.blockykingdom.items.GoblinSkin;
import at.jojokobi.mcutil.NamespacedEntry;
import at.jojokobi.mcutil.entity.ai.ApproachEntityTask;
import at.jojokobi.mcutil.entity.ai.AttackTask;
import at.jojokobi.mcutil.entity.ai.RandomTask;
import at.jojokobi.mcutil.item.ItemHandler;
import at.jojokobi.mcutil.loot.LootInventory;
import at.jojokobi.mcutil.loot.LootItem;
import org.bukkit.event.EventHandler;

//import at.jojokobi.blockykingdom.model.PhantomEmpress;

public class Goblin extends CustomEntity<Zombie> implements Attacker{
	
	public static final NamespacedEntry GOBLIN_SPAWN_KEY = new NamespacedEntry(BlockyKingdomPlugin.BLOCKY_KINGDOM_NAMESPACE, "goblin");
	//public ModeledEntity goblinentity;
	//public ActiveModel goblinmodel;
	public Goblin(Location place, EntityHandler handler) {
		super(place, handler, null);

		setDespawnTicks(5000);
		LootInventory loot = new LootInventory ();
		loot.addItem(new LootItem(0.1, ItemHandler.getItemStack(GoblinSkin.class), 1, 2));
		loot.addItem(new LootItem(0.05, ItemHandler.getItemStack(GoblinFang.class), 1, 1));
		
		addComponent(new LootComponent(loot, 0));
		addComponent(new HealthComponent(new RealHealthAccessor()));

		addEntityTask(new ApproachEntityTask(p -> p.getPassengers().isEmpty() && this.getHandler().getCustomEntityForEntity(p, StoneBeetle.class) != null, 20) {
			@Override
			protected void interact(Entity entity) {
				entity.addPassenger(getEntity());
			}
		});

		addEntityTask(new AttackTask(Player.class));
		addEntityTask(new RandomTask());
	}

	@Override
	public Class<? extends JavaPlugin> getPlugin() {
		return BlockyKingdomPlugin.class;
	}

	@Override
	protected Zombie createEntity(Location place) {
		//ModeledEntity entity = ModelEngineAPI.createModeledEntity(place.getWorld().spawnEntity(place,Zombie.class));

		Zombie entity = place.getWorld().spawn(place, Zombie.class);



		NMSEntityUtil.clearGoals(entity);
		entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(18.0);
		entity.setHealth(entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
		entity.setRemoveWhenFarAway(true);

		ItemStack helmet = new ItemStack(Material.IRON_HOE);
		ItemMeta meta = helmet.getItemMeta();
		meta.setCustomModelData(3);
		meta.setUnbreakable(true);
		helmet.setItemMeta(meta);
		entity.getEquipment().setHelmet(helmet);
		entity.getEquipment().setHelmetDropChance(0);

		/*ItemStack chestplate = new ItemStack(Material.IRON_HOE);
		ItemMeta meta1 = chestplate.getItemMeta();
		meta1.setCustomModelData(8);
		meta1.setUnbreakable(true);
		chestplate.setItemMeta(meta1);
		entity.getEquipment().setChestplate(chestplate);
		entity.getEquipment().setChestplateDropChance(0);

		ItemStack leggings = new ItemStack(Material.IRON_HOE);
		ItemMeta meta2 = leggings.getItemMeta();
		meta2.setCustomModelData(9);
		meta2.setUnbreakable(true);
		leggings.setItemMeta(meta2);
		entity.getEquipment().setLeggings(leggings);
		entity.getEquipment().setLeggingsDropChance(0);
*/
		entity.getEquipment().setItemInMainHandDropChance(0.1f);
		entity.getEquipment().setItemInOffHandDropChance(0.1f);
		//entity.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 1000000, 1, true, false));
		entity.setLootTable(null);
		//entity.setCustomName("Goblin");

		entity.setBaby(true);
		//entity.isBaby(1);
		//goblinentity = ModelEngineAPI.api.getModelManager().createModeledEntity(entity);
		//goblinmodel = ModelEngineAPI.api.getModelManager().createActiveModel("goblin_engineer");

		//goblinentity.getMountHandler().setDriver(entity);


		//goblinentity.getMountHandler().setControllers();
		//goblinentity.getNametagHandler().setCustomName("tag_name", "Goblin");
		//goblinentity.getNametagHandler().setCustomNameVisibility("tag_name", true);
		//goblinentity.detectPlayers();
		//goblinentity.addActiveModel(goblinmodel);


		//goblinentity.setInvisible(true);
		return entity;
	}

	@Override
	public void setSave(boolean save) {
		super.setSave(save);
		getEntity().setRemoveWhenFarAway(save);
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
		//ModelEngineAPI.api.getModelManager().getModeledEntity(getEntity().getUniqueId()).getActiveModel("goblin_engineer").addState("attack1",1,1,1.0);
		entity.damage(2);
		if (getEntity().getEquipment().getItemInMainHand().getType() == Material.AIR && entity instanceof LivingEntity && Math.random() < 0.05) {
			getEntity().getEquipment().setItemInMainHand(((LivingEntity) entity).getEquipment().getItemInMainHand().clone());
			getEntity().getEquipment().setItemInMainHandDropChance(1);
			((LivingEntity) entity).getEquipment().setItemInMainHand(null);
			//ModelEngineAPI.api.getModelManager().getModeledEntity(getEntity().getUniqueId()).getActiveModel("goblin_engineer").addState("attack2",1,1,1.0);
		}


	}

	@Override
	public int getAttackDelay() {
		return 10;
	}
	
	@Override
	protected double getSprintSpeed() {
		return 0.3;
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
