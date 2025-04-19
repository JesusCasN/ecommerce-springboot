// Mostrar/Ocultar contraseña
const togglePassword = document.querySelector('#togglePassword');
const passwordInput = document.querySelector('#password');

togglePassword.addEventListener('click', function () {
    // Alterna entre 'password' y 'text'
    const type = passwordInput.getAttribute('type') === 'password' ? 'text' : 'password';
    passwordInput.setAttribute('type', type);

    // Cambia el ícono de ojo (con o sin línea)
    this.querySelector('i').classList.toggle('fa-eye');
    this.querySelector('i').classList.toggle('fa-eye-slash');
});
