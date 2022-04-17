var btnChangePassword = document.querySelector('#btn-change-password');
var formChangePassword = document.querySelector('.form-change__password');
var upIcon = document.querySelector('.up-icon');
var downIcon = document.querySelector('.down-icon');

btnChangePassword.addEventListener('click' ,function () {
    formChangePassword.classList.toggle('hidden');
    downIcon.classList.toggle('hidden');
    upIcon.classList.toggle('hidden');
})