package lekavar.lma.drinkbeer.entity.damage;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import org.jetbrains.annotations.Nullable;

public class AlcoholDamage extends DamageSource {
    public AlcoholDamage() {
        super("drinkbeer.alcohol");
    }

    @Override
    public Text getDeathMessage(LivingEntity entity) {
        String str = "death.attack." + this.name;
        return new TranslatableText(str, entity.getDisplayName());
    }
}