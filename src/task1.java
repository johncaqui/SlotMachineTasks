package src;

import java.math.BigDecimal;
import java.util.Random;

public class task1
{
    public static void main(String[] args)
    {
        play_game_1(1000000);
        play_game_2(1000000);
    }

    static void play_game_1(int num_of_plays)
    {
        // Define Rand Variable to Roll Dice
        Random rand = new Random();

        int[] game_results = new int[num_of_plays];

        // Define variables to count wins and losses
        int win = 0;
        int lose = 0;

        for(int i = 0; i < num_of_plays; i ++)
        {
            for(int j = 0; j < 4; j ++)
            {
                // Random value from 1 to 6
                int dice = rand.nextInt(6) + 1;

                // If value of dice is 6, player wins
                if (dice == 6)
                {
                    win++;
                    game_results[i] = 1;
                    break;
                }
                // If value of dice is not 6, and reached end of roll number, player loses
                else if (j == 3)
                {
                    lose++;
                    game_results[i] = -1;
                }
            }
        }

        System.out.println("Game 1 Metrics:");

        // Calculate Mean, Variance and Standard Deviation
        calc_metrics(win, lose, game_results);
    }
    static void play_game_2(int num_of_plays)
    {
        // Define Rand Variable to Roll Dice
        Random rand = new Random();

        int[] game_results = new int[num_of_plays];

        // Define variables to count wins and losses
        int win = 0;
        int lose = 0;

        for(int i = 0; i < num_of_plays; i ++)
        {
            for(int j = 0; j < 24; j ++)
            {
                // Random value from 1 to 6
                int dice_1 = rand.nextInt(6) + 1;
                int dice_2 = rand.nextInt(6) + 1;

                // If value of dice is 6, player wins
                if (dice_1 == 6 && dice_2 == 6)
                {
                    win++;
                    game_results[i] = 1;
                    break;
                }
                // If value of dice is not 6, and reached end of roll number, player loses
                else if (j == 23)
                {
                    lose++;
                    game_results[i] = -1;
                }
            }
        }

        System.out.println("Game 2 Metrics:");

        // Calculate Mean, Variance and Standard Deviation
        calc_metrics(win, lose, game_results);
    }

    static void calc_metrics(int win, int lose, int[] game_results)
    {
        System.out.println("\nWins: " + win);
        System.out.println("Losses: " + lose);

        float sum = (float)(win - lose);
        float mean = sum / (float) (win + lose);

        System.out.println("Sum: " + sum);
        System.out.println("Mean: " + mean);

        double variance = 0;

        for(int k : game_results)
        {
            variance += Math.pow(k - mean, 2);
        }

        System.out.println("Variance: "+ new BigDecimal(variance / (double)(game_results.length-1)).doubleValue());

        double standard_deviation = Math.sqrt(variance / (double)(game_results.length));

        System.out.println("Standard Deviation: " + standard_deviation + "\n");

    }
}
