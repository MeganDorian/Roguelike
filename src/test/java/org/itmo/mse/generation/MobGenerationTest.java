package org.itmo.mse.generation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.googlecode.lanterna.TextCharacter;
import java.util.List;
import org.itmo.mse.constants.MobSpecifications;
import org.itmo.mse.constants.ObjectNames;
import org.itmo.mse.constants.SpecialCharacters;
import org.itmo.mse.game.objects.mob.AggressiveMobStrategy;
import org.itmo.mse.game.objects.mob.Mob;
import org.itmo.mse.game.objects.mob.PassiveMobStrategy;
import org.junit.jupiter.api.RepeatedTest;

public class MobGenerationTest {
    
    @RepeatedTest(10)
    public void testMobsGenerate() {
        Mob newMob = MobGeneration.generateMob();
        assertTrue(newMob.getDamage() >= MobSpecifications.lowerMobDamage,
            "Mob's damage is below the lower limit!");
        assertTrue(newMob.getDamage() <= MobSpecifications.upperMobDamage,
            "Mob's damage exceeds the upper limit!");
        assertTrue(newMob.getHealth() >= MobSpecifications.lowerMobHealthy,
            "Mob's health is below the lower limit!");
        assertTrue(newMob.getHealth() <= MobSpecifications.upperMobHealthy,
            "Mob's health exceeds the upper limit!");
        int experience;
        TextCharacter character;
        List<String> names;
        if (newMob.getStrategy().getClass().equals(AggressiveMobStrategy.class)) {
            experience = MobSpecifications.defaultExperienceAggressiveMob;
            character = SpecialCharacters.AGGRESSIVE_MOB;
            names = ObjectNames.aggressiveMobs;
        } else if (newMob.getStrategy().getClass().equals(PassiveMobStrategy.class)) {
            experience = MobSpecifications.defaultExperiencePassiveMob;
            character = SpecialCharacters.PASSIVE_MOB;
            names = ObjectNames.passiveMob;
        } else {
            experience = MobSpecifications.defaultExperienceShyMob;
            character = SpecialCharacters.SHY_MOB;
            names = ObjectNames.shyMob;
        }
        assertEquals(experience, newMob.getExperience());
        assertEquals(character, newMob.getCharacter());
        assertTrue(names.contains(newMob.getName()));
    }
}
