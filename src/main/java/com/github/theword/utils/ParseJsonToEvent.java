package com.github.theword.utils;

import com.github.theword.returnBody.returnModle.MyBaseComponent;
import com.github.theword.returnBody.returnModle.MyHoverEntity;
import com.github.theword.returnBody.returnModle.MyHoverItem;
import com.github.theword.returnBody.returnModle.MyTextComponent;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.*;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ParseJsonToEvent {

    public MutableText parseMessages(List<? extends MyBaseComponent> myBaseComponentList) {
        MutableText mutableText = parsePerMessageToMultiText(myBaseComponentList.get(0));
        for (int i = 1; i < myBaseComponentList.size(); i++) {
            mutableText.append(parsePerMessageToMultiText(myBaseComponentList.get(i)));
        }
        return mutableText;
    }

    public MutableText parsePerMessageToMultiText(MyBaseComponent myBaseComponent) {
        LiteralTextContent literalTextContent = new LiteralTextContent(myBaseComponent.getText());
        Identifier identifier = null;
        if (myBaseComponent.getFont() != null) {
            identifier = new Identifier(Identifier.DEFAULT_NAMESPACE, myBaseComponent.getFont());
        }

        Style style = Style.EMPTY.
                withColor(TextColor.parse(myBaseComponent.getColor()))
                .withBold(myBaseComponent.isBold())
                .withItalic(myBaseComponent.isItalic())
                .withUnderline(myBaseComponent.isUnderlined())
                .withStrikethrough(myBaseComponent.isStrikethrough())
                .withObfuscated(myBaseComponent.isObfuscated())
                .withInsertion(myBaseComponent.getInsertion())
                .withFont(identifier);


        // 配置 TextComponent 额外属性
        if (myBaseComponent instanceof MyTextComponent myTextComponent) {
            if (myTextComponent.getClickEvent() != null) {
                ClickEvent.Action tempAction = ClickEvent.Action.byName(myTextComponent.getClickEvent().getAction());
                ClickEvent clickEvent = new ClickEvent(tempAction, myTextComponent.getClickEvent().getValue());
                style.withClickEvent(clickEvent);
            }
            // TODO 悬浮事件待完善
            if (myTextComponent.getHoverEvent() != null) {
                HoverEvent hoverEvent = null;
                switch (myTextComponent.getHoverEvent().getAction()) {
                    case "show_text" -> {
                        if (myTextComponent.getHoverEvent().getBaseComponentList() != null && myTextComponent.getHoverEvent().getBaseComponentList().size() > 0) {
                            MutableText textComponent = parseMessages(myTextComponent.getHoverEvent().getBaseComponentList());
                            hoverEvent = new HoverEvent(HoverEvent.Action.SHOW_TEXT, textComponent);
                        }
                    }
                    case "show_item" -> {
                        MyHoverItem myHoverItem = myTextComponent.getHoverEvent().getItem();
                        Item item = Item.byRawId(myHoverItem.getId());
                        ItemStack itemStack = new ItemStack(item, myHoverItem.getCount());
                        HoverEvent.ItemStackContent itemStackContent = new HoverEvent.ItemStackContent(itemStack);
                        hoverEvent = new HoverEvent(HoverEvent.Action.SHOW_ITEM, itemStackContent);
                    }
                    case "show_entity" -> {
                        MyHoverEntity myHoverEntity = myTextComponent.getHoverEvent().getEntity();
                        Optional<EntityType<?>> entityType = EntityType.get(myHoverEntity.getType());
                        if (entityType.isPresent()) {
                            HoverEvent.EntityContent entityTooltipInfo = new HoverEvent.EntityContent(entityType.get(), UUID.randomUUID(), MutableText.of(new LiteralTextContent(myHoverEntity.getType())));
                            hoverEvent = new HoverEvent(HoverEvent.Action.SHOW_ENTITY, entityTooltipInfo);
                        }
                    }
                    default -> {
                    }
                }
                style.withHoverEvent(hoverEvent);
            }
        }
        MutableText tempMutableText = MutableText.of(literalTextContent);
        tempMutableText.setStyle(style);
        return tempMutableText;
    }
}
