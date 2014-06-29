package com.awesomesauce.minecraft.forge.rndtech

import com.awesomesauce.minecraft.forge.core.lib.TAwesomeSauceMod
import com.awesomesauce.minecraft.forge.core.lib.util.ItemUtil
import com.awesomesauce.minecraft.forge.rndtech.api._
import com.awesomesauce.minecraft.forge.rndtech.item.ItemJournal
import com.awesomesauce.minecraft.forge.rndtech.tile.TileEntityResearcher
import cpw.mods.fml.common.{ModMetadata, Mod}
import cpw.mods.fml.common.event.FMLInitializationEvent
import cpw.mods.fml.common.event.FMLPostInitializationEvent
import cpw.mods.fml.common.event.FMLPreInitializationEvent
import net.minecraft.block.Block
import net.minecraft.block.material.Material
import net.minecraft.item.Item
import cpw.mods.fml.common.Mod.EventHandler
import net.minecraft.item.ItemStack
import net.minecraft.init.Items
import net.minecraft.init.Blocks

@Mod(modid=RnDTech.MODID, name=RnDTech.MODNAME, modLanguage="scala")
object RnDTech extends TAwesomeSauceMod {

  @EventHandler
  def aspri(e: FMLPreInitializationEvent) = super.awesomesaucepreinit(e)
  @EventHandler
  def asi(e: FMLInitializationEvent) = super.awesomesauceinit(e)
  @EventHandler
  def aspoi(e: FMLPostInitializationEvent) = super.awesomesaucepostinit(e)
  var researcher : Block = null
  var builder : Block = null
  var crank : Block = null
  var converter : Block = null
  var journal : Item = null
  var researchDust : Item = null
  final val MODID = "RnDTech"
  final val MODNAME = "RnDTech"
  def getModID: String = MODID
  def getModName: String = MODNAME
  def getTabIconItem: () => net.minecraft.item.Item = () => journal
  def getTextureDomain: String = "rndtech"
  @Mod.Metadata("RnDTech")
  var metadata : ModMetadata = null
  
  def preInit() = {}
  def init() = {
    researcher = ItemUtil.makeBlock(this, "researcher", Material.iron, () => new TileEntityResearcher)
    journal = ItemUtil.makeItem(this, "journal", new ItemJournal)
    researchDust = ItemUtil.makeItem(this, "researchDust")
    RnDTechAPI.addResearch(new Research(new RnDRecipeBasic(
        new ItemStack(Items.redstone), 
        Set(
            new ItemStack(Items.redstone, 10), 
            new ItemStack(Items.iron_ingot)),
        new ItemStack(researchDust, 10)), new RnDRecipeBasic(
            new ItemStack(Items.redstone),
            Set(
                new ItemStack(Items.redstone, 10), 
                new ItemStack(Blocks.planks,2),
                new ItemStack(researchDust, 1)),
                new ItemStack(builder)), 
                "builder","Builder", "You have learned how to build\nitems from your research."))
  }
  def postInit() = {}
}
