function displayPopUp(artifactId) {
    let modal = document.getElementById("modal" + artifactId);
    modal.style.display = "block";
}

function closePopUp(artifactId) {
    let modal = document.getElementById("modal"+ artifactId);
    modal.style.display = "none";
}

function closePopUpAnywhere(artifactId) {
    let modal = document.getElementById("modal"+ artifactId);
    window.onclick = function (ev) {
        if (ev.target === modal) {
            modal.style.display = "none"
        }
    }
}

