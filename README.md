# CurrencyXchange


## 🌍 Overview
RealTime-CurrencyConverter is a professional **real-time currency conversion** web application built with **Spring Boot (Java Full Stack)** and **React**. It automatically detects the user's location and suggests the **local currency**, fetching **live exchange rates** and displaying country flags.

---

## 🚀 Features
✅ Real-time currency conversion using **ExchangeRate-API** / **Frankfurter API**  
✅ Automatic location detection to display local currency  
✅ Dropdown selection for source & target currencies  
✅ Country flags displayed for an intuitive experience  
✅ Beautiful and professional UI/UX  
✅ Fully responsive design (mobile & desktop)  
✅ Backend built with **Spring Boot**  
✅ Frontend powered by **React** with API integration  

---

## ⚙️ Tech Stack
- **Backend**: Spring Boot (REST API, Thymeleaf, Spring Security)
- **Frontend**: React, Bootstrap, Axios
- **Database**: MySQL (Optional: User Preferences, History)
- **APIs**: ExchangeRate-API / Frankfurter API
- **Extras**: Geolocation API (Detects user location)

---

## 📥 Installation & Setup
### **🔧 Backend Setup (Spring Boot)**

```bash
git clone https://github.com/yourusername/CurrencyXchange.git
cd backend
mvn clean install
mvn spring-boot:run
```

### **🌐 Frontend Setup (React)**
```bash
cd frontend
npm install
npm start
```

---

## 📡 API Configuration
1. **Get a free API key** from [ExchangeRate-API](https://www.exchangerate-api.com/) or [Frankfurter API](https://www.frankfurter.app/).
2. Add your API key in the `.env` file:
   ```env
   REACT_APP_EXCHANGE_RATE_API_KEY=your_api_key_here
   ```

---

## 📜 License
This project is licensed under the **MIT License** - see the [LICENSE](LICENSE) file for details.

---

## 👨‍💻 Author
**Oussama Zbair** - [GitHub Profile](https://github.com/oussama-zbair)

🌟 If you like this project, give it a ⭐ on GitHub!
