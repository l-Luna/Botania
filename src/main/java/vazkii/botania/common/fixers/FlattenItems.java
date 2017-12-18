package vazkii.botania.common.fixers;

import net.minecraft.item.EnumDyeColor;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.IFixableData;
import vazkii.botania.common.lib.LibItemNames;
import vazkii.botania.common.lib.LibMisc;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.function.IntFunction;

public class FlattenItems implements IFixableData {
	// old id to (meta -> new id)
	private static final Map<String, IntFunction<String>> LOOKUP = new HashMap<>();

	static {
		LOOKUP.put("botania:manaresource", i -> {
			if(i == 10) {
				// support ancient prismarine from 1.7
				return "minecraft:prismarine_shard";
			} else {
				return LibMisc.MOD_ID + ":" + LibItemNames.MANA_RESOURCE_NAMES[i % LibItemNames.MANA_RESOURCE_NAMES.length];
			}
		});
		LOOKUP.put("botania:vial", i -> {
			if(i == 1) {
				return LibMisc.MOD_ID + ":" + LibItemNames.FLASK;
			} else {
				return LibMisc.MOD_ID + ":" + LibItemNames.VIAL;
			}
		});
		LOOKUP.put("botania:quartz", i -> LibMisc.MOD_ID + ":" + LibItemNames.QUARTZ_NAMES[i % LibItemNames.QUARTZ_NAMES.length]);
		LOOKUP.put("botania:thornchakram", i -> {
			if(i == 1) {
				 return LibMisc.MOD_ID + ":" + LibItemNames.FLARE_CHAKRAM;
			} else {
				return LibMisc.MOD_ID + ":" + LibItemNames.THORN_CHAKRAM;
			}
		});
		LOOKUP.put("botania:grasshorn", i -> {
			switch (i) {
				case 0:
				default:
				return LibMisc.MOD_ID + ":" + LibItemNames.GRASS_HORN;
				case 1: return LibMisc.MOD_ID + ":" + LibItemNames.LEAVES_HORN;
				case 2: return LibMisc.MOD_ID + ":" + LibItemNames.SNOW_HORN;
			}
		});
		LOOKUP.put("botania:lens", i -> LibMisc.MOD_ID + ":" + LibItemNames.LENS_NAMES[i % LibItemNames.LENS_NAMES.length]);
		LOOKUP.put("botania:dye", i -> {
			EnumDyeColor color = EnumDyeColor.byMetadata(i);
			return LibMisc.MOD_ID + ":" + LibItemNames.DYE + "_" + color.getName();
		});
		LOOKUP.put("botania:petal", i -> {
			EnumDyeColor color = EnumDyeColor.byMetadata(i);
			return LibMisc.MOD_ID + ":" + LibItemNames.PETAL + "_" + color.getName();
		});
	}

	@Override
	public int getFixVersion() {
		return 1;
	}

	@Nonnull
	@Override
	public NBTTagCompound fixTagCompound(@Nonnull NBTTagCompound compound) {
		String id = compound.getString("id");
		if(LOOKUP.containsKey(id)) {
			int meta = compound.getShort("Damage");
			compound.setString("id", LOOKUP.get(id).apply(meta));
			compound.removeTag("Damage");
		}
		return compound;
	}
}