package org.itmo.mse.game.objects.items;

import static org.itmo.mse.constants.SpecialCharacters.MEDICAL_AID;

import com.googlecode.lanterna.TerminalPosition;

public class MedicalAid extends Item {
    public MedicalAid(TerminalPosition start) {
        super(start, MEDICAL_AID);
    }
}
