package org.itmo.mse.generation;

import com.googlecode.lanterna.TextCharacter;
import org.itmo.mse.constants.ObjectNames;
import org.itmo.mse.constants.Proportions;
import org.itmo.mse.constants.SpecialCharacters;
import org.itmo.mse.constants.MobSpecifications;
import org.itmo.mse.game.objects.mob.*;

import java.util.List;

public class MobGeneration extends Generation {
    
    /**
     * Generate random mob
     *
     * @return mob
     */
    public static Mob generateMob() {
        MobStrategy strategy;
        List<String> allNames;
        TextCharacter character;
        int damage = (int) (rand.nextDouble() * (MobSpecifications.upperMobDamage - MobSpecifications.lowerMobDamage) +
                            MobSpecifications.lowerMobDamage);
        int health = (int) (rand.nextDouble() * (MobSpecifications.upperMobHealthy - MobSpecifications.lowerMobHealthy) +
                            MobSpecifications.lowerMobHealthy);
        int experience;
        int generateRandomStrategy = rand.nextInt(100);
        if (generateRandomStrategy < Proportions.shyMob * 100) {
            strategy = new ShyMobStrategy();
            allNames = ObjectNames.shyMob;
            experience = MobSpecifications.defaultExperienceShyMob;
            character = SpecialCharacters.SHY_MOB;
        } else if (generateRandomStrategy < (Proportions.shyMob + Proportions.passiveMob) * 100) {
            strategy = new PassiveMobStrategy();
            allNames = ObjectNames.passiveMob;
            experience = MobSpecifications.defaultExperiencePassiveMob;
            character = SpecialCharacters.PASSIVE_MOB;
        } else {
            strategy = new AggressiveMobStrategy();
            allNames = ObjectNames.aggressiveMobs;
            experience = MobSpecifications.defaultExperienceAggressiveMob;
            character = SpecialCharacters.AGGRESSIVE_MOB;
        }
        return new Mob(generatePosition(),
                       character,
                       allNames.get(rand.nextInt(allNames.size())),
                       strategy,
                       damage,
                       health,
                       experience);
    }
}
