function validate_form(event) {
    var form = event.target;
    if(form.elements.newPassword.value !== form.elements.confirmNewPassword.value) {
        event.preventDefault();
        $("#modal_message").html("Пароли не совпадают");
        show_modal();
        form.elements.newPassword.value = form.elements.confirmNewPassword.value = "";
    }
}