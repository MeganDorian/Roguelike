package org.itmo.mse.game.objects.items;

import static org.itmo.mse.constants.SpecialCharacters.MEDICAL_AID;

import com.googlecode.lanterna.TerminalRectangle;

public class MedicalAid extends Item {
    public MedicalAid(TerminalRectangle position) {
        super(position, MEDICAL_AID);
    }
}
