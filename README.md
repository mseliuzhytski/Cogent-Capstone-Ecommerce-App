
# ESHOP


## Description
Welcome to EShop, your ultimate destination for all your shopping needs! EShop is not just your average online store; it's a curated marketplace where you can discover a diverse range of products tailored to suit every aspect of your lifestyle. Whether you're a culinary enthusiast seeking the freshest ingredients, a gaming aficionado on the hunt for the latest releases, or a DIY enthusiast looking for workshop essentials, EShop has got you covered.

With an intuitive interface and seamless navigation, EShop makes it effortless to explore and find exactly what you're looking for. Our extensive catalog boasts a wide array of categories, ensuring that there's something for everyone. From premium produce and gourmet ingredients to cutting-edge electronics and handcrafted artisanal goods, EShop offers a captivating shopping experience like no other.

Shop with confidence knowing that each product on EShop is carefully curated for quality and authenticity. With secure checkout options and hassle-free returns, we prioritize your satisfaction and strive to make every shopping experience memorable.

Join the EShop community today and embark on a journey of discovery, convenience, and unparalleled shopping bliss. Welcome to EShop – where shopping becomes an adventure!
## Features
#### For Customers:
* **Extensive Product Selection:** Explore a diverse array of products on the shop page, ranging from culinary essentials to gaming accessories and everything in between.
* **Category Filters:**** Easily find products by filtering them based on categories, ensuring a tailored shopping experience.
* **Smart Search Functionality:** Seamlessly search for specific products using the intuitive search bar, saving time and effort.
* **Sort Options:** Sort products by price, date added, or name to quickly find what you're looking for.
* **Convenient Cart Management:** Maintain a cart to store selected items for easy checkout, allowing for a streamlined shopping process.
* **Personalized Wishlist:** Create and manage a wishlist of desired items, making it simple to keep track of products for future purchases
* **Discount Application:** Apply discounts to orders before submitting, maximizing savings on your purchases.
* **Order History:** Access your order history on your user profile page, enabling you to track past purchases and review details.

#### For Admins:
* **Product Management:** Perform CRUD operations (Create, Read, Update, Delete) on products, ensuring accurate and up-to-date product listings.
* **Bulk Upload:** Instead of adding products manually, upload an Excel file of a list of products. See the file `src/main/resources/bulk-upload.xlsx` for an example.
* **Category Management:** Perform CRUD operations (Create, Read, Update, Delete) on categories.
* **User Account Management:** Create new user accounts and manage existing ones to facilitate a smooth user experience.
* **Discount Management:** Create and manage discounts to offer promotional pricing and enhance customer satisfaction.
* **Sales Reporting:** Generate comprehensive sales reports based on various parameters to gain insights into business performance and trends.


## Tech Stack

**Backend Development:** Java (Spring Boot), Eureka Sever , Spring Security, MySQL

**Frontend Development:** Angular

**Deployment:** AWS

## Powerpoint

Our [Powerpoint Presentation](capstone.pptx)

## Run Locally

#### Prerequisites
- Java Development Kit (JDK) installed on your machine.
- Node.js and npm (Node Package Manager) installed for frontend development.
- MySQL database set up locally for local usage.

#### Steps

1. **Clone Repositories:**
- Clone The following Repositories:
	- Main Service (Current Repository)
	- [Server Repository](https://github.com/mseliuzhytski/Cogent-Capstone-Ecommerce-Service-Registry)
	- [Gateway Repository](https://github.com/mseliuzhytski/Cogent-Capstone-Ecommerce-Gateway)
	- [Sales Report Service Repository](https://github.com/mseliuzhytski/Cogent-Capstone-Ecommerce-Sales-Report)
	- [Discount Service Repository](https://github.com/mseliuzhytski/Cogent-Capstone-Ecommerce-Discounts)
	- [Frontend Repository](https://github.com/mseliuzhytski/Cogent-Capstone-Ecommerce-Frontend)

2. **Setup Local MySQL Database:**
- On the Main Service, Discount Service, and Sales Report Service Spring Boot applications, go to application properties under the resources folder
- Configure your local MySQL database with the correct credentials (username, password, URL)
  - You will set your database details on the fields: (spring.datasource)
  - For example: For your username: **spring.datasource.username=yourusername**
  - *Note: You should remove previous database configurations, only one set of database properties should be present*
- Ensure on first launch that you have these properties: **spring.sql.init.mode=always**
- On Subsequent Launches make sure the property is set to none: **spring.sql.init.mode=none**
- If you want to use some user-provided data including an administrator username and password (specified in application.properties), then issue the POST request: localhost:8080/databaseloader

3. **Run Spring Boot Applications:**
- Run the Server Spring Boot application first, followed by the Gateway Spring Boot application, and then the remaining Spring Boot applications.

4. **Install Frontend Dependencies:**
- Navigate to the Frontend Repository directory.
- Run `npm install` to install the required dependencies.

5. **Serve Angular Project:**
- Run `ng serve` in the Frontend Repository directory to serve the Angular project in development mode.
- Ensure that the Angular project is configured to use the correct service URLs for communication with the backend services.

6. **Access the Application:**
- Once all services are up and running, you should be able to access and fully utilize the application. You can access the application on the port mentioned on the angular application.

## Deploy
See this [file](deployment.md).

## Authors

- [@chrisgioia64](https://github.com/chrisgioia64)
- [@mseliuzhytski](https://github.com/mseliuzhytski)
- [@rafirafi2](https://github.com/rafirafi2)


## Acknowledgements

- Cogent Info Tech

## Link
[Deployed App](http://54.85.70.2/)


