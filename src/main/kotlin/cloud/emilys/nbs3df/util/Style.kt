package cloud.emilys.nbs3df.util

import net.minecraft.ChatFormatting
import net.minecraft.network.chat.MutableComponent

fun MutableComponent.important() = this.withStyle(ChatFormatting.BOLD, ChatFormatting.YELLOW)