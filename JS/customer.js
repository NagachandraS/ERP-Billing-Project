console.log("ViewCustomer JS loaded");
document.addEventListener("DOMContentLoaded", function () {

    document.getElementById("searchButton").addEventListener("click", searchCustomerByName);

    document.getElementById("customerName").addEventListener("keydown", function (event) {
            if (event.key === "Enter") {
                event.preventDefault(); // Prevent form submission, if any
                searchCustomerByName(event);
            }
        });
});
//document.addEventListener("DOMContentLoaded", function () {
//
//    console.log("DOM loaded");
//
//    const searchButton = document.getElementById("searchButton");
//
//    console.log("Search button element:", searchButton);
//
//    if (searchButton) {
//        searchButton.addEventListener("click", searchCustomerByName);
//    }
//
//    const customerName = document.getElementById("customerName");
//
//    console.log("Customer input element:", customerName);
//
//});

async function deleteCustomer(id) {
   
    if (!confirm("Are you sure you want to delete " + id + " ?"))
        return;

    try {
        const response = await fetch(`/api/customers/${id}`, { method: "DELETE" });

        if (response.ok) {
            window.location.reload();
            alert("Customer Deleted Successfully");
            
        } else {
            alert("Failed to delete customer");
        }

    } catch (error) {
        console.log(error);
        alert("Something went wrong");
    }
}

async function deleteVendor(id)
{

    if(!confirm("Are you sure you want to delete " + id + " ?"))

        return;

        try{
            const response= await fetch(`/api/vendor/${id}`, { method: "DELETE" });

if (response.ok) {
            window.location.reload();
            alert("Customer Deleted Successfully");
            
        } else {
            alert("Failed to delete customer");
        }

        }
        catch (error){
            console.log(error);
        alert("Something went wrong");

        }

}

async function searchCustomerByName()
{
console.log("search function called");
const name= document.getElementById("customerName").value;
console.log("sending name  is ",name )
apiCall("/customers/view/search?name="+encodeURIComponent(name),"GET", null, function(response)
{
if(response.success)
{
loadCustomers(response.data);
}
else {
showErrorMessage(response.message);
}

},false)
}

function loadCustomers(customersList)
{

const tableBody = document.getElementById("tableBody");
tableBody.innerHTML ="";
customersList.forEach(customer=>
{
const row = document.createElement("tr");

const nameCell = document.createElement("td");
nameCell.textContent= customer.customerName;

const phoneCell = document.createElement("td");
phoneCell.textContent= customer.customerPhoneNumber;

const addressCell = document.createElement("td");
addressCell.textContent= customer.customerAddress;

const mailCell = document.createElement("td");
mailCell.textContent= customer.customerEmail;

row.appendChild(nameCell);
row.appendChild(phoneCell);
row.appendChild(addressCell);
row.appendChild(mailCell);

tableBody.appendChild(row);
});
}

