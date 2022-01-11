package lk.ijse.pos_system.view.tm;

public class ReportTM {
     private String itemCode;
    private double lost;
    private double profit;
    private int sales;
    private double income;

    public ReportTM() {
    }

    public ReportTM(String itemCode,int sales, double income,double lost, double profit) {
        this.setItemCode(itemCode);
        this.setLost(lost);
        this.setProfit(profit);
        this.setSales(sales);
        this.setIncome(income);
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public double getLost() {
        return lost;
    }

    public void setLost(double lost) {
        this.lost = lost;
    }

    public double getProfit() {
        return profit;
    }

    public void setProfit(double profit) {
        this.profit = profit;
    }

    public int getSales() {
        return sales;
    }

    public void setSales(int sales) {
        this.sales = sales;
    }

    public double getIncome() {
        return income;
    }

    public void setIncome(double income) {
        this.income = income;
    }
}
