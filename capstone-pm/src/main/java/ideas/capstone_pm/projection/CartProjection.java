package ideas.capstone_pm.projection;

public interface CartProjection {
    Integer getCartId();
    Double getPlannedInvestment();
    CartItemsProjection getFund();

    interface CartItemsProjection {
        Integer getFundId();
        String getFundName();
    }
}
