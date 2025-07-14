document.getElementById("login-form").addEventListener("submit", async (e) => {
    e.preventDefault();
    const username = document.getElementById("username").value;
    const password = document.getElementById("password").value;

    const response = await fetch("/login", {
        method: "POST",
        headers: {
            "Content-Type": "application/x-www-form-urlencoded"
        },
        body: `username=${encodeURIComponent(username)}&password=${encodeURIComponent(password)}`,
        credentials: "include"
    });

    const result = await response.json();
    document.getElementById("login-message").innerText = result.message || result.error;

    if (response.ok) {
        document.getElementById("login-section").style.display = "none";
        document.getElementById("logout-section").style.display = "block";

        // Fetch role from backend if needed
        if (username === "default_engineer") {
            document.getElementById("engineer-section").style.display = "block";
        } else {
            document.getElementById("customer-section").style.display = "block";
        }
    }
});

document.getElementById("logout-btn").addEventListener("click", async () => {
    const response = await fetch("/logout", {
        method: "POST",
        credentials: "include"
    });

    const result = await response.json();
    alert(result.message || result.error);

    location.reload(); // Reset UI
});

document.getElementById("register-customer-form").addEventListener("submit", async (e) => {
    e.preventDefault();
    const username = document.getElementById("customer-username").value;
    const password = document.getElementById("customer-password").value;

    const response = await fetch("/customers", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        credentials: "include",
        body: JSON.stringify({ username, password })
    });

    const result = await response.json();
    alert(result.message || result.error);
});

document.getElementById("create-ticket-form").addEventListener("submit", async (e) => {
    e.preventDefault();
    const title = document.getElementById("ticket-title").value;
    const description = document.getElementById("ticket-description").value;

    const response = await fetch("/tickets", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        credentials: "include",
        body: JSON.stringify({ title, description })
    });

    const result = await response.json();
    alert(result.message || result.error);
});
