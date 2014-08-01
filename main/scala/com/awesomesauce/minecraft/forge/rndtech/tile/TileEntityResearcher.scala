package com.awesomesauce.minecraft.forge.rndtech.tile

import java.util.NoSuchElementException

import com.awesomesauce.minecraft.forge.core.lib.item.{TActivatedTileEntity, TRedstonePulseActivated}
import com.awesomesauce.minecraft.forge.core.lib.util.WorldUtil
import com.awesomesauce.minecraft.forge.rndtech.api.RnDTechAPI
import com.awesomesauce.minecraft.forge.rndtech.item.ItemJournal
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.IInventory
import net.minecraft.item.ItemStack
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.ChunkCoordinates

class TileEntityResearcher extends TileEntity with IInventory with TActivatedTileEntity with TRedstonePulseActivated {
  val inventory = scala.collection.mutable.Map[Int, ItemStack]()

  def closeInventory(): Unit = {}

  def decrStackSize(slot: Int, amount: Int): ItemStack = {
    if (inventory(slot) == null) {
      return null
    }
    val i = inventory(slot).copy()
    i.stackSize = amount
    inventory(slot).stackSize -= amount
    return i
  }

  def getInventoryName(): String = "Researcher"

  def getInventoryStackLimit(): Int = 1

  def getSizeInventory(): Int = 2

  def getStackInSlotOnClosing(slot: Int): ItemStack = null

  def hasCustomInventoryName(): Boolean = false

  def isItemValidForSlot(slot: Int, stack: ItemStack): Boolean = {
    if (slot == 0) {
      return stack.getItem().isInstanceOf[ItemJournal]
    }
    return true
  }

  def isUseableByPlayer(p: EntityPlayer): Boolean = true

  def openInventory(): Unit = {}

  def activate(player: EntityPlayer, side: Int, partx: Float, party: Float, partz: Float): Boolean = {
    if (player.inventory.getCurrentItem() == null || player.inventory.getCurrentItem().stackSize == 0) {
      if (getStackInSlot(0) != null && getStackInSlot(0).stackSize != 0) {
        player.inventory.addItemStackToInventory(getStackInSlot(0))
        setInventorySlotContents(0, null)
      } else if (getStackInSlot(1) != null && getStackInSlot(1).stackSize != 0) {
        player.inventory.addItemStackToInventory(getStackInSlot(1))
      }
    } else if (player.inventory.getCurrentItem().getItem().isInstanceOf[ItemJournal] && getStackInSlot(0) == null) {
      setInventorySlotContents(0, player.inventory.getCurrentItem().copy())
      player.inventory.getCurrentItem().stackSize -= 1
    } else if (getStackInSlot(1) != null && getStackInSlot(1).stackSize == 0) {
      setInventorySlotContents(1, player.inventory.getCurrentItem().copy())
      player.inventory.getCurrentItem().stackSize = 0
    }

    tryToCraft()
    return true
  }

  def setInventorySlotContents(slot: Int, stack: ItemStack): Unit = inventory.put(slot, stack)

  def pulse() = {
    tryToCraft()
  }

  def tryToCraft() = {
    for (i <- RnDTechAPI.getResearchIterator) {
      if ((!i.isResearchDone(getStackInSlot(0))) && i.getRecipe.doesItemMatch(getStackInSlot(1))) {
        val block = WorldUtil.scanForBlockTileEntityInstanceOf[IInventory](worldObj, new ChunkCoordinates(xCoord, yCoord, zCoord))
        for (x <- block) {
          val te = worldObj.getTileEntity(x.posX.toInt, x.posY.toInt, x.posZ.toInt).asInstanceOf[IInventory]
          if (getStackInSlot(1).stackSize >= 1 && i.getRecipe.craftRecipe(te)) {
            println("Researching.")
            i.addResearchToJournal(getStackInSlot(0))
            getStackInSlot(1).stackSize -= 1
          }
        }
      }
    }
  }

  def getStackInSlot(slot: Int): ItemStack = try {
    inventory(slot)
  } catch {
    case e: NoSuchElementException => null
  }
}
