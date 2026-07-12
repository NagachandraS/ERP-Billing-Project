function showToast(message, cssClass)
{
    const toast = document.getElementById("toast");

    toast.innerText = message;
    toast.className = "toast " + cssClass;
    toast.style.display = "block";

    setTimeout(function() {
        toast.style.display = "none";
    }, 3000);
}

function showSuccessMessage(message)
{
showToast(message, "toast-success");
}
function showErrorMessage(message)
{
 showToast(message, "toast-error");
}


function apiCall(url, method, data, onSuccess, showMessage = true)
{
    fetch(url, {
        method: method,
        headers: {
            "Content-Type": "application/json"
        },
        body: data ? JSON.stringify(data) : null
    })
    .then(response => {
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        return response.json();
    })
    .then(result => {

        if (showMessage) {
            if (result.success) {
                showSuccessMessage(result.message);
            } else {
                showErrorMessage(result.message);
            }
        }

        // Always invoke the callback
        if (onSuccess) {
            onSuccess(result);
        }
    })
    .catch(error => {
        showErrorMessage("Unexpected error occurred");
        console.error(error);
    });
}