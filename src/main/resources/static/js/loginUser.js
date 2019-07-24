function loginUser() {
    let login = document.getElementById("login-name").value;
    if(login === "admin") {
        window.location.href = "html/admin/admin.html";
    } else if(login === "mentor") {
        window.location.href = "html/mentor/mentor.html";
    } else if(login === "codecooler") {
        window.location.href = "html/user/user.html";
    } else {
        alert("Invalid login");
    }
}

function logoutUser() {
        window.location.href = "../../index.html";
}