package com.upik.model;

import com.google.android.gms.maps.model.LatLng;

public class NearByLocations {

    private LatLng latLng;

    String JobTitle,Timing,Agent,TotalPay,Lat,Long,JobID,CategoryID,JobTime,JobImage,WorkingHours,YourDescription,
            Requirement, ArrivalInstuction,CreatedDate,UpdatedDate,Deleted,Miles,Rating,merchant,IsFavorite,
            Background, Date, StartDate, EndDate, UrgentListing, HourlyPay, PayType, Admin_Comment, Admin_Rating, MerchantID;

    public NearByLocations(LatLng latLng, String JobTitle, String Timing, String Agent, String TotalPay, String Lat, String Long,
                           String JobID, String CategoryID, String JobTime, String JobImage, String WorkingHours, String YourDescription,
                           String Requirement, String ArrivalInstuction, String CreatedDate, String Miles, String Rating,String merchant,
                           String IsFavorite, String Background, String Date, String StartDate, String EndDate,
                           String UrgentListing, String HourlyPay, String PayType, String Admin_Comment, String Admin_Rating, String MerchantID) {
        this.latLng = latLng;
        this.JobTitle = JobTitle;
        this.Timing = Timing;
        this.Agent = Agent;
        this.TotalPay = TotalPay;
        this.Lat = Lat;
        this.Long = Long;
        this.JobID = JobID;
        this.CategoryID = CategoryID;
        this.JobTime = JobTime;
        this.JobImage = JobImage;
        this.WorkingHours = WorkingHours;
        this.YourDescription = YourDescription;
        this.Requirement = Requirement;
        this.ArrivalInstuction = ArrivalInstuction;
        this.CreatedDate = CreatedDate;
        this.Miles = Miles;
        this.Rating = Rating;
        this.merchant = merchant;
        this.IsFavorite = IsFavorite;
        this.Background = Background;
        this.Date = Date;
        this.StartDate = StartDate;
        this.EndDate = EndDate;
        this.UrgentListing = UrgentListing;
        this.HourlyPay = HourlyPay;
        this.PayType = PayType;
        this.Admin_Comment = Admin_Comment;
        this.Admin_Rating = Admin_Rating;
        this.MerchantID = MerchantID;
    }

    public String getAdmin_Comment() {
        return Admin_Comment;
    }

    public void setAdmin_Comment(String admin_Comment) {
        Admin_Comment = admin_Comment;
    }

    public String getAdmin_Rating() {
        return Admin_Rating;
    }

    public void setAdmin_Rating(String admin_Rating) {
        Admin_Rating = admin_Rating;
    }

    public String getStartDate() {
        return StartDate;
    }

    public void setStartDate(String startDate) {
        StartDate = startDate;
    }

    public String getEndDate() {
        return EndDate;
    }

    public void setEndDate(String endDate) {
        EndDate = endDate;
    }

    public String getUrgentListing() {
        return UrgentListing;
    }

    public void setUrgentListing(String urgentListing) {
        UrgentListing = urgentListing;
    }

    public String getHourlyPay() {
        return HourlyPay;
    }

    public void setHourlyPay(String hourlyPay) {
        HourlyPay = hourlyPay;
    }

    public String getPayType() {
        return PayType;
    }

    public void setPayType(String payType) {
        PayType = payType;
    }

    public String getMerchant() {
        return merchant;
    }

    public String getIsFavorite() {
        return IsFavorite;
    }

    public void setIsFavorite(String isFavorite) {
        IsFavorite = isFavorite;
    }

    public void setMerchant(String merchant) {
        this.merchant = merchant;
    }

    public String getBackground() {
        return Background;
    }

    public void setBackground(String background) {
        Background = background;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getLat() {
        return Lat;
    }

    public void setLat(String lat) {
        Lat = lat;
    }

    public String getLong() {
        return Long;
    }

    public void setLong(String aLong) {
        Long = aLong;
    }

    public String getJobTitle() {
        return JobTitle;
    }

    public void setJobTitle(String jobTitle) {
        JobTitle = jobTitle;
    }

    public String getTiming() {
        return Timing;
    }

    public void setTiming(String timing) {
        Timing = timing;
    }

    public String getAgent() {
        return Agent;
    }

    public void setAgent(String agent) {
        Agent = agent;
    }

    public String getTotalPay() {
        return TotalPay;
    }

    public void setTotalPay(String totalPay) {
        TotalPay = totalPay;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }


    public String getJobID() {
        return JobID;
    }

    public void setJobID(String jobID) {
        JobID = jobID;
    }

    public String getCategoryID() {
        return CategoryID;
    }

    public void setCategoryID(String categoryID) {
        CategoryID = categoryID;
    }

    public String getJobTime() {
        return JobTime;
    }

    public void setJobTime(String jobTime) {
        JobTime = jobTime;
    }

    public String getJobImage() {
        return JobImage;
    }

    public void setJobImage(String jobImage) {
        JobImage = jobImage;
    }

    public String getWorkingHours() {
        return WorkingHours;
    }

    public void setWorkingHours(String workingHours) {
        WorkingHours = workingHours;
    }

    public String getYourDescription() {
        return YourDescription;
    }

    public void setYourDescription(String yourDescription) {
        YourDescription = yourDescription;
    }

    public String getRequirement() {
        return Requirement;
    }

    public void setRequirement(String requirement) {
        Requirement = requirement;
    }

    public String getArrivalInstuction() {
        return ArrivalInstuction;
    }

    public void setArrivalInstuction(String arrivalInstuction) {
        ArrivalInstuction = arrivalInstuction;
    }

    public String getCreatedDate() {
        return CreatedDate;
    }

    public void setCreatedDate(String createdDate) {
        CreatedDate = createdDate;
    }

    public String getUpdatedDate() {
        return UpdatedDate;
    }

    public void setUpdatedDate(String updatedDate) {
        UpdatedDate = updatedDate;
    }

    public String getDeleted() {
        return Deleted;
    }

    public void setDeleted(String deleted) {
        Deleted = deleted;
    }
    public String getMiles() {
        return Miles;
    }

    public void setMiles(String miles) {
        Miles = miles;
    }

    public String getRating() {
        return Rating;
    }

    public void setRating(String rating) {
        Rating = rating;
    }

    public String getMerchantID() {
        return MerchantID;
    }

    public void setMerchantID(String merchantID) {
        MerchantID = merchantID;
    }
}
