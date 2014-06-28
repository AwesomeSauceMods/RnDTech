package com.awesomesauce.minecraft.forge.rndtech.api

object RnDTechAPI {
	val researches = scala.collection.mutable.Set[Research]()
	def addResearch(r:Research) = {
	  researches.add(r)
	}
	def getResearchIterator = researches.iterator
}