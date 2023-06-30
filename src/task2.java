package src;

import org.apache.commons.math3.distribution.EnumeratedDistribution;
import org.apache.commons.math3.util.Pair;
import java.util.*;

public class task2
{
    public static void main(String[] args)
    {
        int sim_count = 10000000;
        int payout = 0;
        int cost = 0;
        int win_payout = 0;
        int win_count = 0;
        int bonus_payout = 0;
        int bonus_count = 0;

        for(int i = 0; i < sim_count; i ++)
        {
            cost += 10;

            // Play reels and get reel pictures
            String[][] reels_output = play_reels();

            // Check paylines to see if player won
            List<String[]> unique_symbols_wins = check_paylines(reels_output);

            // Check bonuses to see if player won a bonus round
            boolean bonus_round = check_bonus_round(reels_output);

            int symbols_pay = 0;

            // Calculate payout of symbols win
            if(unique_symbols_wins.size() > 0)
            {
                symbols_pay = calculate_symbols_win(unique_symbols_wins);
                win_count += 1;
            }

            int bonus_pay = 0;

            // Calculate bonus round payout
            if(bonus_round == true)
            {
                bonus_count += 1;
                bonus_pay = calculate_bonus_win();
            }

            win_payout += symbols_pay;
            bonus_payout += bonus_pay;
            payout += symbols_pay + bonus_pay;
        }

        System.out.println("Number of Plays: " + sim_count);
        System.out.println("Total Cost: " + cost);
        System.out.println("Total Payout: " + payout);
        System.out.println("Return to Player Percentage: " + ((float) payout / (float) cost));
        System.out.println("Win Count: " + win_count);
        System.out.println("Average Win Payout: " + ((float) win_payout / (float) win_count));
        System.out.println("Bonus Count: " + bonus_count);
        System.out.println("Average Bonus Payout: " + ((float) bonus_payout / (float) bonus_count));
    }

    public static String[][] play_reels()
    {
        // Define symbols of all reels
        String[] reel0 = {"L4", "L3", "L2", "B1", "H2", "H2", "H2", "L1", "L1", "L1", "H3", "H1", "H2",
                "L2", "L2", "L2", "W1", "L3", "L3", "L3", "H3", "H3", "H3", "L4", "L4", "L4", "H3", "L1",
                "L2", "L4", "H3", "L3", "L3", "L3", "B1", "L2", "L2", "L2", "L2", "H3", "H3", "H3", "L4",
                "L4", "L4", "B1", "L1", "L1", "L1", "H2", "L2", "L2", "L2", "B1", "L3", "L3", "L3"};
        String[] reel1 = {"L4", "L3", "L2", "B1", "H2", "H2", "H2", "L1", "L1", "L1", "H3", "H1", "H2",
                "L2", "L2", "L2", "W1", "L3", "L3", "L3", "H3", "H3", "H3", "L4", "L4", "L4", "H3", "L1",
                "L2", "L4", "H3", "L3", "L3", "L3", "B1", "L2", "L4", "H3", "H3", "H3", "B1", "L2", "L2",
                "L2", "L2", "B1", "L3", "L3", "L3", "H1", "L4", "L4", "L4", "L3"};
        String[] reel2 = {"L4", "L3", "L2", "B1", "H2", "H2", "H2", "L1", "L1", "L1", "H3", "H1", "H2",
                "L2", "L2", "L2", "W1", "L3", "L3", "L3", "H3", "H3", "H3", "L4", "L4", "L4", "H3", "L1",
                "L2", "L4", "H3", "L3", "L3", "L3", "B1", "L2", "H2", "H3", "H3", "H3", "L2", "L2", "L2",
                "L1", "L1", "B1", "L2", "L2", "L2", "L1"};

        String[][] reels = {reel0, reel1, reel2};

        Random rand = new Random();

        String[][] output_reels = new String[3][3];

        // Loop through reels to choose symbol for each reel
        for(int i = 0; i < reels.length; i ++)
        {
            int reel_stop = rand.nextInt(reels[i].length);

            output_reels[0][i] = reels[i][reel_stop];

            // if symbol after reel stop goes out of array range, loop back to beginning by getting first element
            if (reel_stop + 1 >= reels[i].length)
            {
                output_reels[1][i] = reels[i][(reel_stop) - (reels[i].length-1)];
                output_reels[2][i] = reels[i][(reel_stop + 1) - (reels[i].length-1)];
            }
            else if (reel_stop + 2 >= reels[i].length)
            {
                output_reels[1][i] = reels[i][reel_stop+1];
                output_reels[2][i] = reels[i][(reel_stop + 1) - (reels[i].length-1)];
            }
            else
            {
                output_reels[1][i] = reels[i][(reel_stop + 1)];
                output_reels[2][i] = reels[i][(reel_stop + 2)];
            }
        }
        return output_reels;
    }

    public static List<String[]> check_paylines(String[][] output_reels)
    {
        // Define the corrodinates for each square for each payline
        Integer[][][] paylines = {{{0, 0}, {0, 1}, {0, 2}},
                                 {{1, 0}, {1, 1}, {1, 2}},
                                 {{2, 0}, {2, 1}, {2, 2}},
                                 {{0, 0}, {1, 1}, {2, 2}},
                                 {{2, 0}, {1, 1}, {0, 2}}};

        String[] symbols = new String[3];

        List<String[]> unique_symbols_wins = new ArrayList<>();

        // Check Paylines to see if player won anything
        for(Integer[][] payline : paylines)
        {
            for (int j = 0; j < payline.length; j++)
            {
                String symbol = output_reels[payline[j][0]][payline[j][1]];

                symbols[j] = symbol;
            }

            String[] unique_symbols = Arrays.stream(symbols).distinct().toArray(String[]::new);

            // If Payline only contains 1 symbol which isn't bonus, then it is a winning symbol
            if (unique_symbols.length == 1 && !Arrays.asList(unique_symbols).contains("B1"))
            {
                unique_symbols_wins.add(unique_symbols);
            }
            // If Payline contains 2 symbols which aren't bonus, and which contains 2 winning symbols, with one being a wild symbol, then it is a winning symbol
            else if (unique_symbols.length == 2 && Arrays.asList(unique_symbols).contains("W1") && !Arrays.asList(unique_symbols).contains("B1"))
            {
                unique_symbols_wins.add(unique_symbols);
            }
        }

        return unique_symbols_wins;
    }

    public static boolean check_bonus_round(String[][] output_reels)
    {
        int bonus_count = 0;

        // Check Bonus to see if player has bonus
        for(String[] row : output_reels)
        {
            for(String symbol : row)
            {
                if(symbol == "B1") bonus_count++;
            }
        }

        if(bonus_count >= 3) return true;

        return false;
    }

    public static int calculate_symbols_win(List<String[]> unique_symbols_wins)
    {
        Dictionary<String, Integer> symbol_winnings_dict = new Hashtable<>();

        symbol_winnings_dict.put("W1", 2000);
        symbol_winnings_dict.put("H1", 800);
        symbol_winnings_dict.put("H2", 400);
        symbol_winnings_dict.put("H3", 80);
        symbol_winnings_dict.put("L1", 60);
        symbol_winnings_dict.put("L2", 20);
        symbol_winnings_dict.put("L3", 16);
        symbol_winnings_dict.put("L4", 12);

        int symbols_pay = 0;

        // For each unique symbol win, check whether 1 or 2 unique symbols are found
        for(String[] unique_symbols : unique_symbols_wins)
        {
            // If 1 is found, then get the value of that symbol from the dictionary
            if (unique_symbols.length == 1)
            {
                symbols_pay += symbol_winnings_dict.get(unique_symbols[0]);
            }
            // If 2 are found, then get the value of the symbol which isn't a wild symbol from the dictionary
            else
            {
                for(int j = 0; j < unique_symbols.length; j ++)
                {
                    if (unique_symbols[j] != "W1") symbols_pay += symbol_winnings_dict.get(unique_symbols[j]);
                }
            }
        }

        return symbols_pay;
    }

    public static int calculate_bonus_win()
    {
        Random rand = new Random();

        int bonus_pay = 0;

        List<Pair<Integer, Double>> coin_weight_pairs = new ArrayList<>();

        coin_weight_pairs.add(new Pair<>(100, 10.0));
        coin_weight_pairs.add(new Pair<>(50, 20.0));
        coin_weight_pairs.add(new Pair<>(30, 40.0));
        coin_weight_pairs.add(new Pair<>(20, 50.0));
        coin_weight_pairs.add(new Pair<>(10, 151.0));
        coin_weight_pairs.add(new Pair<>(5, 220.0));
        coin_weight_pairs.add(new Pair<>(4, 260.0));
        coin_weight_pairs.add(new Pair<>(3, 300.0));
        coin_weight_pairs.add(new Pair<>(2, 350.0));

        // Define Enumerated Distribution which easily allows for weighted random coin selection
        EnumeratedDistribution coin_weight_distribution = new EnumeratedDistribution<>(coin_weight_pairs);

        // Randomly choose coin from defined coins and their weights
        int coin_val = (int) coin_weight_distribution.sample();

        // Randomly choose dice for multiplier
        int dice_val = rand.nextInt(6) + 1;

        int bet_val = 10;

        bonus_pay = coin_val * dice_val * bet_val;

        return bonus_pay;
    }
}