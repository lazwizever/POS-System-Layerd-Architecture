package lk.ijse.pos_system.dto;

public class ItemDTO {
    private String itemCode;
    private String description;
    private String packSize;
    private double unitPrice;
    private double discount;
    private int qyOnHand;

    public ItemDTO() {
    }

    public ItemDTO(String itemCode, String description, String packSize, double unitPrice, double discount, int qyOnHand) {
        this.setItemCode(itemCode);
        this.setDescription(description);
        this.setPackSize(packSize);
        this.setUnitPrice(unitPrice);
        this.setDiscount(discount);
        this.setQyOnHand(qyOnHand);
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPackSize() {
        return packSize;
    }

    public void setPackSize(String packSize) {
        this.packSize = packSize;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public int getQyOnHand() {
        return qyOnHand;
    }

    public void setQyOnHand(int qyOnHand) {
        this.qyOnHand = qyOnHand;
    }
}
