package kapitalBot;

public class BotFactory {
    private static BotInterface bot;
    public static BotInterface getBot(String icon, int playerID, String botType)
    {
        if (botType.equals("Aggressive"))
            bot = new AggressiveBot(icon, playerID);
        else if (botType.equals("Dummy"))
            bot =  new DummyBot(icon, playerID);
        else if (botType.equals("Passive"))
            bot =  new PassiveBot(icon, playerID);
        else if (botType.equals("Reasonable"))
            bot =  new ReasonableBot(icon, playerID);
        return bot;
    }
}
