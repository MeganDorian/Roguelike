package org.itmo.mse.constants;

public interface ObjectDescription {
    String usualAid = "Restores " + ObjectEffect.usualAid * 100 + "% of health";
    String legendaryAid = "Restores " + ObjectEffect.legendaryAid * 100 + "% of health";
    String usualLightWeapon = "Increases attack by " + ObjectEffect.light;
    String usualMediumWeapon = "Increases attack by " + ObjectEffect.medium;
    String legendaryHardWeapon = "Increases attack by " + ObjectEffect.heavy;
    String usualLightArmor = "Increases armor by " + ObjectEffect.light;
    String usualMediumArmor = "Increases armor by " + ObjectEffect.medium;
    String legendaryHardArmor = "Increases armor by " + ObjectEffect.heavy;
}
