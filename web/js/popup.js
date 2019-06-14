function displayPopUp() {
    let modal = document.getElementById("modal");
    modal.style.display = "block";
}

function closePopUp() {
    let modal = document.getElementById("modal");
    modal.style.display = "none";
}

function closePopUpAnywhere() {
    let modal = document.getElementById("modal");
    window.onclick = function (ev) {
        if (ev.target === modal) {
            modal.style.display = "none"
        }
    }
}

