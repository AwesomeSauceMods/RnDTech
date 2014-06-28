package com.awesomesauce.minecraft.forge.rndtech.item

import com.awesomesauce.minecraft.forge.core.lib.item.ItemDescription
import net.minecraft.item.Item
import net.minecraft.item.ItemWritableBook
import com.awesomesauce.minecraft.forge.core.lib.item.ItemReadableBook
import net.minecraft.item.ItemStack

class ItemJournal extends ItemReadableBook("Research Journal", "You") {
  addPage("Research Journal:\nI have decided to keep a journal of all my\n endeavors within researching new technologies.\nHope it ends well!")
}