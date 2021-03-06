package yio.tro.antiyoy.gameplay;

import yio.tro.antiyoy.factor_yio.FactorYio;

import java.util.ArrayList;

public class MoveZoneManager {


    private final MoveZoneDetection moveZoneDetection;
    FieldController fieldController;
    public ArrayList<Hex> moveZone;
    public FactorYio appearFactor;


    public MoveZoneManager(FieldController fieldController) {
        this.fieldController = fieldController;

        moveZone = new ArrayList<Hex>();
        appearFactor = new FactorYio();
        moveZoneDetection = new MoveZoneDetection(fieldController);
    }


    public void move() {
        appearFactor.move();
    }


    public void checkToClear() {
        if (moveZone.size() <= 0) return;
        if (appearFactor.get() >= 0.01) return;

        clear();
    }


    public void clear() {
        for (Hex hex : moveZone) {
            hex.inMoveZone = false;
        }
        moveZone.clear();
    }


    public boolean isHexInMoveZone(Hex hex) {
        return moveZone.contains(hex);
    }


    public void addHexToMoveZoneManually(Hex hex) {
        moveZone.add(hex);
        hex.inMoveZone = true;
    }


    public void removeHexFromMoveZoneManually(Hex hex) {
        moveZone.remove(hex);
        hex.inMoveZone = false;
    }


    public void hide() {
        appearFactor.destroy(1, 5);
        getSelectionController().getBlackoutFactor().destroy(1, 5);
    }


    public ArrayList<Hex> detectMoveZone(Hex startHex, int strength) {
        return moveZoneDetection.detectMoveZone(startHex, strength);
    }


    public ArrayList<Hex> detectMoveZone(Hex startHex, int strength, int moveLimit) {
        return moveZoneDetection.detectMoveZone(startHex, strength, moveLimit);
    }


    public void detectAndShowMoveZone(Hex startHex, int strength) {
        detectAndShowMoveZone(startHex, strength, 9001); // move limit is almost infinite
    }


    public void detectAndShowMoveZone(Hex startHex, int strength, int moveLimit) {
        moveZone = moveZoneDetection.detectMoveZone(startHex, strength, moveLimit);
        checkToForceMoveZoneAnims();
        appearFactor.setValues(0, 0);
        appearFactor.appear(3, 1.5);
        getGameController().selectionController.getBlackoutFactor().appear(3, 1.5);
    }


    public void detectAndShowMoveZoneForBuildingUnit(int strength) {
        detectAndShowMoveZone(fieldController.selectedHexes.get(0), strength);
    }


    private SelectionController getSelectionController() {
        return getGameController().selectionController;
    }


    public void detectAndShowMoveZoneForFarm() {
        moveZone = moveZoneDetection.detectMoveZoneForFarm();
        checkToForceMoveZoneAnims();
        appearFactor.setValues(0, 0);
        appearFactor.appear(3, 1.5);
        getSelectionController().getBlackoutFactor().appear(3, 1.5);
    }


    private GameController getGameController() {
        return fieldController.gameController;
    }


    public void defaultValues() {
        appearFactor.setValues(0, 0);
    }


    public void checkToForceMoveZoneAnims() {
        if (moveZone.get(0).selectionFactor.get() == 1) return;

        for (Hex hex : moveZone) {
            hex.animFactor.setValues(1, 0);
        }
    }
}
