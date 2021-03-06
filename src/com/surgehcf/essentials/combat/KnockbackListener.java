package com.surgehcf.essentials.combat;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerVelocityEvent;
import org.bukkit.util.Vector;

import com.surgehcf.SurgeCore;
import com.surgehcf.core.hcf.VarService;
import com.surgehcf.core.hcf.timer.GlobalTimer;
import com.surgehcf.core.hcf.timer.Timer;
import com.surgehcf.essentials.SurgeExtra;


public class KnockbackListener
implements Listener
{
	private Field fieldPlayerConnection;
	private Method sendPacket;
	private Constructor<?> packetVelocity;

	public KnockbackListener()
	{
		try
		{
			Class<?> entityPlayerClass = Class.forName("net.minecraft.server." + SurgeExtra.getInstance().getCraftBukkitVersion() + ".EntityPlayer");
			Class<?> packetVelocityClass = Class.forName("net.minecraft.server." +	SurgeExtra.getInstance().getCraftBukkitVersion() + ".PacketPlayOutEntityVelocity");
			Class<?> playerConnectionClass = Class.forName("net.minecraft.server." + SurgeExtra.getInstance().getCraftBukkitVersion() + ".PlayerConnection");


			this.fieldPlayerConnection = entityPlayerClass.getField("playerConnection");
			this.sendPacket = playerConnectionClass.getMethod("sendPacket", new Class[] { packetVelocityClass.getSuperclass() });
			this.packetVelocity = packetVelocityClass.getConstructor(new Class[] { Integer.TYPE, Double.TYPE, Double.TYPE, Double.TYPE });
		}
		catch (ClassNotFoundException|NoSuchFieldException|SecurityException|NoSuchMethodException e)
		{
			e.printStackTrace();
		}
	}


	public double getKnockbackVert(){

		double kbvert = VarService.KB_VERT;
		return kbvert;
	}

	public double getKnockbackHorz(){

		double kbhoriz = VarService.KB_HOR;
		return kbhoriz;
	}

	/*
	 * Edit Knockback -  horizontal: 1.03
 | vertical: 1.07
	 */
	@EventHandler
	public void onPlayerVelocity(PlayerVelocityEvent event)
	{
		Player player = event.getPlayer();
		EntityDamageEvent lastDamage = player.getLastDamageCause();
		if ((lastDamage == null) || (!(lastDamage instanceof EntityDamageByEntityEvent))) {
			return;
		}
		if ((((EntityDamageByEntityEvent)lastDamage).getDamager() instanceof Player)) {
			event.setCancelled(true);
		}
	}

	@EventHandler(priority=EventPriority.HIGHEST)
	public void onEntityDamageByEntity(EntityDamageByEntityEvent event)
	{
		for(Timer timer : SurgeCore.getInstance().getTimerManager().getTimers()){
			if ((timer instanceof GlobalTimer)) {
				if(timer.getName().equalsIgnoreCase(SurgeCore.getPlugin().getTimerManager().sotw.getName())){
					return;
				}

			}
		}
		if ((!(event.getEntity() instanceof Player)) || (!(event.getDamager() instanceof Player))) {
			return;
		}
		if (event.isCancelled()) {
			return;
		}
		Player damaged = (Player)event.getEntity();
		Player damager = (Player)event.getDamager();
		if (damaged.getNoDamageTicks() > damaged.getMaximumNoDamageTicks() / 2.0D) {
			return;
		}
		double horMultiplier = this.getKnockbackHorz();
		double verMultiplier = this.getKnockbackVert();
		double sprintMultiplier = damager.isSprinting() ? 0.8D : 0.5D;
		double kbMultiplier = damager.getItemInHand() == null ? 0.0D : damager.getItemInHand().getEnchantmentLevel(Enchantment.KNOCKBACK) * 0.2D;

		@SuppressWarnings("deprecation")
		double airMultiplier = damaged.isOnGround() ? 1.0D : 0.5D;

		Vector knockback = damager.getLocation().getDirection().normalize();
		knockback.setX((knockback.getX() * sprintMultiplier + kbMultiplier) * horMultiplier);
		knockback.setY(0.35D * airMultiplier * verMultiplier);
		knockback.setZ((knockback.getZ() * sprintMultiplier + kbMultiplier) * horMultiplier);
		try
		{
			Object entityPlayer = damaged.getClass().getMethod("getHandle", new Class[0]).invoke(damaged, new Object[0]);
			Object playerConnection = this.fieldPlayerConnection.get(entityPlayer);
			Object packet = this.packetVelocity.newInstance(new Object[] { Integer.valueOf(damaged.getEntityId()), Double.valueOf(knockback.getX()), Double.valueOf(knockback.getY()), Double.valueOf(knockback.getZ()) });
			this.sendPacket.invoke(playerConnection, new Object[] { packet });
		}
		catch (SecurityException|IllegalArgumentException|IllegalAccessException|InvocationTargetException|NoSuchMethodException|InstantiationException e)
		{
			e.printStackTrace();
		}
	}
}
