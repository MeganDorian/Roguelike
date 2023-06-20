package org.itmo.mse.generation;

import com.googlecode.lanterna.TextCharacter;
import java.util.List;
import org.itmo.mse.constants.MobStrategy;
import org.itmo.mse.constants.ObjectNames;
import org.itmo.mse.constants.Proportions;
import org.itmo.mse.constants.SpecialCharacters;
import org.itmo.mse.constants.Specifications;
import org.itmo.mse.game.objects.Mob;

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
        int damage = (int) (rand.nextDouble() *
                            (Specifications.upperMobDamage - Specifications.lowerMobDamage)
                            + Specifications.lowerMobDamage);
        int health = (int) (rand.nextDouble() *
                          (Specifications.upperMobHealthy - Specifications.lowerMobHealthy)
                          + Specifications.lowerMobHealthy);
        int experience;
        int generateRandomStrategy = rand.nextInt(100);
        if (generateRandomStrategy < Proportions.cowardlyMob * 100) {
            strategy = MobStrategy.COWARDLY;
            allNames = ObjectNames.cowardlyMob;
            experience = Specifications.defaultExperienceCowardlyMob;
            character = SpecialCharacters.COWARDLY_MOB;
        } else if(generateRandomStrategy < (Proportions.cowardlyMob + Proportions.passiveMob) * 100) {
            strategy = MobStrategy.PASSIVE;
            allNames = ObjectNames.passiveMob;
            experience = Specifications.defaultExperiencePassiveMob;
            character = SpecialCharacters.PASSIVE_MOB;
        } else {
            strategy = MobStrategy.AGGRESSIVE;
            allNames = ObjectNames.aggressiveMobs;
            experience = Specifications.defaultExperienceAggressiveMob;
            character = SpecialCharacters.AGGRESSIVE_MOB;
        }
        return new Mob(generatePosition(), character, allNames.get(rand.nextInt(allNames.size()))
            , strategy,
            damage, health, experience);
    }
}
