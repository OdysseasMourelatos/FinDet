public abstract class BudgetEntry {
    private int code;
    private String description;
    private String category;
    private long amount;

    public BudgetEntry(int code, String description, String category, long amount) {
        this.code = code;
        this.description = description;
        this.category = category;
        this.amount = amount;
    }

    public int getCode() {
        return this.code;
    }

    public String getDescription() {
        return this.description;
    }

    public String getCategory(){
        return this.category;
    }

    public long getAmount(){
        return this.amount;
    }

    public void setAmount(long amount) {
        if (amount>0) {
            this.amount = amount;
        } else {
            System.out.println("Το ποσό δεν μπορεί να είναι αρνητικό");
        }
    }

    @Override
    public String toString(){
        return "Code: " + code + ", Description: " + description + ", Category: " + category + ", Amount: " + amount;
    }
}


