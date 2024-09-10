package ideas.capstone_pm.dto;

public interface CartProjection {
    Integer getCartId();
    Double getPlannedInvestment();
    CartItemsProjection getFund();

    interface CartItemsProjection {
        String getFundName();
    }
}
