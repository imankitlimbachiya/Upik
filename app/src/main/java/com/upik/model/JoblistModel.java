package com.upik.model;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Admin on 5/25/2019.
 */

public class JoblistModel {

    private LatLng latLng;

    String JobApplyID, JobID, CategoryID, JobTitle, JobTime, JobImage, PayType, TotalPay, WorkingHours, YourDescription, Admin_Rating,
            Requirement, ArrivalInstuction, CreatedDate, UpdatedDate, Deleted, Lat, Long, Miles, Rating, Timing, Admin_Comment,
            Agent, HourlyPay, merchant, Phone, Email, isFavorite, Background, Date, StartDate, EndDate, UrgentListing, MerchantID;

    public String getAdmin_Rating() {
        return Admin_Rating;
    }

    public void setAdmin_Rating(String admin_Rating) {
        Admin_Rating = admin_Rating;
    }

    public String getAdmin_Comment() {
        return Admin_Comment;
    }

    public void setAdmin_Comment(String admin_Comment) {
        Admin_Comment = admin_Comment;
    }

    public String getPayType() {
        return PayType;
    }

    public void setPayType(String payType) {
        PayType = payType;
    }

    public String getHourlyPay() {
        return HourlyPay;
    }

    public void setHourlyPay(String hourlyPay) {
        HourlyPay = hourlyPay;
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

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getBackground() {
        return Background;
    }

    public void setBackground(String background) {
        Background = background;
    }

    public String getJobApplyID() {
        return JobApplyID;
    }

    public String getIsFavorite() {
        return isFavorite;
    }

    public void setIsFavorite(String isFavorite) {
        this.isFavorite = isFavorite;
    }

    public void setJobApplyID(String jobApplyID) {
        JobApplyID = jobApplyID;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getMerchant() {
        return merchant;
    }

    public void setMerchant(String merchant) {
        this.merchant = merchant;
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

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
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

    public String getJobImage() {
        return JobImage;
    }

    public void setJobImage(String jobImage) {
        JobImage = jobImage;
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

    public String getJobTitle() {
        return JobTitle;
    }

    public void setJobTitle(String jobTitle) {
        JobTitle = jobTitle;
    }

    public String getJobTime() {
        return JobTime;
    }

    public void setJobTime(String jobTime) {
        JobTime = jobTime;
    }

    public String getTotalPay() {
        return TotalPay;
    }

    public void setTotalPay(String totalPay) {
        TotalPay = totalPay;
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

    public String getMerchantID() {
        return MerchantID;
    }

    public void setMerchantID(String merchantID) {
        MerchantID = merchantID;
    }
}
