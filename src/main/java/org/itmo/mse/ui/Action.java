package org.itmo.mse.ui;


/**
 * Actions performed by key pressed
 */
public enum Action {
    
    /**
     * User pressed escape
     */
    EXIT,
    
    /**
     * Waiting user to press enter
     */
    WAIT_TO_PRESS_ENTER,
    
    /**
     * User pressed enter
     */
    PRESSED_ENTER,
    
    /**
     * User didn't press any button to exit game
     */
    CONTINUE_PLAYING
}
