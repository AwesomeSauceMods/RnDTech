package com.awesomesauce.minecraft.forge.rndtech.api

import net.minecraft.inventory.IInventory
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import com.awesomesauce.minecraft.forge.rndtech.item.ItemJournal
import com.awesomesauce.minecraft.forge.core.lib.util.InventoryUtil

trait RnDRecipe {
  def craftRecipe(inv: IInventory): Boolean
  def doesItemMatch(item: ItemStack): Boolean
  def getItemName():String
  def getItemStacks():Array[ItemStack]
}
class RnDRecipeBasic(item : ItemStack, items: Set[ItemStack], result: ItemStack) extends RnDRecipe {
  def craftRecipe(inv:IInventory) : Boolean = {
    for (i <- items)
    {
    	if (!InventoryUtil.scanInventoryForItems(inv, i))
    	  return false
    }
    for (i <- items)
    {
    	for (slot <- Range(0, inv.getSizeInventory()))
    	{
    	  if (inv.getStackInSlot(slot) != null && i.isItemEqual(inv.getStackInSlot(slot)) && inv.getStackInSlot(slot).stackSize >= i.stackSize)
    	  {
    	    inv.getStackInSlot(slot).stackSize -= i.stackSize
    	  }
    	}
    }
    InventoryUtil.addStackToInventory(inv, result)
    return false
  }
  def doesItemMatch(i:ItemStack):Boolean = i != null && item.isItemEqual(i)
  def getItemName():String = item.getDisplayName()
  def getItemStacks():Array[ItemStack] = {
    items.toArray[ItemStack]
  }
}
class Research(recipe: RnDRecipe, productRecipe: RnDRecipe, id:String, name: String, description: String) {
  def getRecipe: RnDRecipe = recipe
  def getProductRecipe: RnDRecipe = productRecipe
  def getName: String = name
  def getDescription: String = description
  def isResearchDone(journal: ItemStack): Boolean = 
  {
  return journal != null && journal.getTagCompound() != null && journal.getTagCompound().getCompoundTag("research") != null && journal.getTagCompound().getCompoundTag("research").getBoolean(id)}
  def addResearchToJournal(journal: ItemStack): Boolean = {
    if (journal.getTagCompound() != null)
      journal.setTagCompound(new NBTTagCompound())
    if (journal.getTagCompound().getCompoundTag("research") == null)
      journal.getTagCompound().setTag("research", new NBTTagCompound())
    if (isResearchDone(journal)) 
      { println("Research already done.")
      return false
      
      }
    journal.getTagCompound().getCompoundTag("research").setBoolean(id, true)
    val item = journal.getItem().asInstanceOf[ItemJournal]
    item.addPage(journal, name+":\n\n"+description+getRecipe.getItemName)
    var string = "Items Required:\n"
    for (i <- getRecipe.getItemStacks)
    {
      string = string + i.stackSize + "x "+ i.getDisplayName()
    }
    item.addPage(journal, string)
    return true
  }
}
