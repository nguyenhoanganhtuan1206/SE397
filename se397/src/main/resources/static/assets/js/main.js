var modal = document.querySelector('.modal__common');
var btnConfirmOrder = document.querySelector('.btn-confirm__order');
var formConfirm = document.querySelector('.form__confirm-modal');
var btnCancel = document.querySelectorAll('.cancel');

/* when click btn confirm order */
function hidden() {
    modal.classList.remove('open');
}

function show() {
    modal.classList.add('open');
}

btnConfirmOrder.addEventListener('click' , show);

modal.addEventListener('click' , hidden);

formConfirm.addEventListener('click' , function (e) {
    e.stopPropagation();
});

document.getElementById('btn__cancel-confirm').addEventListener('click' , function (e) {
    e.preventDefault();
})

for(const btn of btnCancel) {
    btn.addEventListener('click' , hidden);
}
