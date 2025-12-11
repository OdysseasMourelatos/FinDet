import java.util.ArrayDeque;

public class UndoButton implements Command{
    public static Deque<ArrayList<? extends BudgetExpense>> historyList = new ArrayDeque<>();
    public static Deque<? extends ExpeneseFilter> historyFilter = new ArrayDeque<>();
    public setState(){
        
    }
    @Override execute(int times){};
}