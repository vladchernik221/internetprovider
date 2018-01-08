$(document).ready(function() {
   $(".only-for-limit").css({ display: "none" });
});

function change_is_limit(set_limit) {
    if(set_limit) {
        document.forms["tariff_plan_form"].elements["includedTraffic"] = 0;
        document.forms["tariff_plan_form"].elements["priceOverTraffic"] = 0;
        $(".only-for-limit").css({ display: "block" });
    } else {
        document.forms["tariff_plan_form"].elements["includedTraffic"] = "";
        document.forms["tariff_plan_form"].elements["priceOverTraffic"] = "";
        $(".only-for-limit").css({ display: "none" });
    }
}