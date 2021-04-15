javascript:(() => {

    const mobileInput = document.getElementById('MobileNumber');
    const passwordInput = document.getElementById('Password');

    mobileInput.value = '%s';
    passwordInput.value = '%s';

    const loginBtn = document.getElementById('Login');
    loginBtn.click();

})()