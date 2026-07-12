document.addEventListener("DOMContentLoaded", function(){

document.getElementById("customerForm").addEventListener("submit",addCustomer);
document.getElementById("name").addEventListener("input", () => clearErrorMessages("name", "nameError"));
document.getElementById("phone").addEventListener("input",restrictPhone);
document.getElementById("phone").addEventListener("blur", validatePhoneNumber);
})


async function addCustomer()
{
event.preventDefault();
const name = document.getElementById("name").value;
const phone = document.getElementById("phone").value;
const address = document.getElementById("address").value;
const email = document.getElementById("email").value;

let valid=true;

if (!name)
{
document.getElementById("nameError").innerText="Please Enter Name";
document.getElementById("name").classList.add("error-field");
valid=false;
}
if (!phone)
{
document.getElementById("phoneError").innerText="Please Enter Phone Number";
document.getElementById("phone").classList.add("error-field");
valid=false;
}

if (!valid)
{
return;
}

const CustomerForm={
customerName: name,
customerPhoneNumber: phone,
customerAddress: address,
customerEmail: email
}

apiCall("/customers/save","POST",CustomerForm, function(success)
{
console.log(CustomerForm);
console.log(success);
document.getElementById("customerForm").reset();
},true)
}

function clearErrorMessages(filedId, errorId)
{
const field= document.getElementById(filedId);

if(field.value.trim())
{
document.getElementById(errorId).innerText = "";
field.classList.remove("error-field");
}

}

function restrictPhone()
{
    const phone = document.getElementById("phone");
    phone.value = phone.value.replace(/[^0-9]/g, "");
    clearErrorMessages("phone", "phoneError");
}

async function validatePhoneNumber()
{
const phone = document.getElementById("phone").value.trim();
apiCall("/customer/phoneNumber?number=" + encodeURIComponent(phone),"GET",null,function(response){
if(!response.success)
{
document.getElementById("phoneError").innerText = response.message;
console.log(response.message);
document.getElementById("phone").classList.add("error-field");
}
}, false)
}