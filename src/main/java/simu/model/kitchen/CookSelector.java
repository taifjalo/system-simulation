package simu.model.kitchen;

import eduni.distributions.Uniform;
import java.util.ArrayList;

/**
 * Handles cook selection logic for kitchen operations.
 * Uses a uniform distribution to select cooks based on availability and competency ratios.
 */
public class CookSelector {
    /**
     * Uniform distribution for percentage-based cook selection.
     * Used to randomly select cooks according to defined competency ratios.
     */
    private final Uniform cookSelector;

    /**
     * Constructs a CookSelector with a uniform distribution from 0 to 100.
     */
    public CookSelector() {
        this.cookSelector = new Uniform(0, 100); // 0-100 for percentage-based selection
    }

    /**
     * Represents the result of cook selection, including index, competency, and random percentage used.
     */
    public static class CookSelection {
        /** Index of the selected cook in the cooks list. */
        private final int cookIndex;
        /** Competency level of the selected cook. */
        private final CookCompetency competency;
        /** Random percentage value used for selection. */
        private final double randomPercentage;

        /**
         * Constructs a CookSelection result.
         * @param cookIndex index of the selected cook
         * @param competency competency level of the selected cook
         * @param randomPercentage random percentage value used for selection
         */
        public CookSelection(int cookIndex, CookCompetency competency, double randomPercentage) {
            this.cookIndex = cookIndex;
            this.competency = competency;
            this.randomPercentage = randomPercentage;
        }

        /**
         * Gets the index of the selected cook.
         * @return the cook index
         */
        public int getCookIndex() { return cookIndex; }
        /**
         * Gets the competency level of the selected cook.
         * @return the cook's competency
         */
        public CookCompetency getCompetency() { return competency; }
        /**
         * Gets the random percentage value used for selection.
         * @return the random percentage
         */
        public double getRandomPercentage() { return randomPercentage; }
        /**
         * Checks if the selected cook is an expert.
         * @return true if expert, false otherwise
         */
        public boolean isExpert() { return competency == CookCompetency.EXPERT; }
    }

    /**
     * Selects a cook from the available cooks based on competency distribution.
     * Distribution: 33% chance for expert cook, 67% chance for inexperienced cook.
     *
     * @param cooks List of available cooks
     * @return CookSelection containing the selected cook index and details
     */
    public CookSelection selectCook(ArrayList<Cook> cooks) {
        double randomPercentage = cookSelector.sample();
        int selectedCookIndex;
        CookCompetency selectedCompetency;

        if (randomPercentage <= 33.0) {
            // Select expert cook (index 0) - 33% chance
            selectedCookIndex = 0;
            selectedCompetency = CookCompetency.EXPERT;
        } else {
            // Select inexperienced cook (index 1 or 2) - 67% chance
            // Randomly choose between cook 1 and cook 2
            selectedCookIndex = (randomPercentage <= 66.5) ? 1 : 2;
            selectedCompetency = CookCompetency.INEXPERIENCED;
        }

        return new CookSelection(selectedCookIndex, selectedCompetency, randomPercentage);
    }
}