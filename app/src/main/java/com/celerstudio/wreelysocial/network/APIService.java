package com.celerstudio.wreelysocial.network;

import com.celerstudio.wreelysocial.BuildConfig;
import com.celerstudio.wreelysocial.models.BasicResponse;
import com.celerstudio.wreelysocial.models.Event;
import com.celerstudio.wreelysocial.models.MeetingRoomSlot;
import com.celerstudio.wreelysocial.models.MobileVerification;
import com.celerstudio.wreelysocial.models.NearbyWorkspace;

import java.util.List;

import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by sayagodshala on 31/10/17.
 */

public interface APIService {

    @POST(BuildConfig.PATH + "/member/login")
    Observable<Response<BasicResponse>> login(@Body MobileVerification mvr);

    @POST(BuildConfig.PATH + "/member/login/confirmation")
    Observable<Response<BasicResponse>> loginConfirmation(@Body MobileVerification mvr);

    @GET(BuildConfig.PATH + "/member")
    Observable<Response<BasicResponse>> getMemberDetails(@Query("access_token") String accessToken);

    @GET(BuildConfig.PATH + "/vendors")
    Observable<Response<BasicResponse>> getMemberVendors(@Query("access_token") String accessToken);

    @GET(BuildConfig.PATH + "/{vendorId}/companies")
    Observable<Response<BasicResponse>> getCompanies(@Path("vendorId") Long vendorId, @Query("access_token") String accessToken);

    @GET(BuildConfig.PATH + "/{vendorId}/members")
    Observable<Response<BasicResponse>> getVendorMembers(@Path("vendorId") Long vendorId, @Query("access_token") String accessToken);

    @GET(BuildConfig.PATH + "/{vendorId}/companies/{companyId}/members")
    Observable<Response<BasicResponse>> getCompanyMembers(@Path("vendorId") String vendorId, @Path("companyId") String companyId, @Query("access_token") String accessToken);

    @GET(BuildConfig.PATH + "/{vendorId}/meetingrooms")
    Observable<Response<BasicResponse>> getMeetingRooms(@Path("vendorId") String vendorId, @Query("access_token") String accessToken);

    //    http://localhost:8888/wreely/services/v2/7/meetingroom/bookings/room_id=3&start_date=2018-06-06 12:00:00&end_date=2018-06-06 12:50:00?access_token=97e3ed57cbe89cd1fe673799ccffcdee
    @GET(BuildConfig.PATH + "/{vendorId}/meetingroom/bookings/room_id={roomId}&start_date={startDate} 00:00:00&end_date={endDate} 23:59:59")
    Observable<Response<BasicResponse>> getMeetingRoomSlots(@Path("vendorId") String vendorId, @Path("roomId") int roomId, @Path("startDate") String startDate, @Path("endDate") String endDate, @Query("access_token") String accessToken);

    @POST(BuildConfig.PATH + "/{vendorId}/meetingroom/bookings")
    Observable<Response<BasicResponse>> bookMeetingRoom(@Path("vendorId") String vendorId, @Body MeetingRoomSlot slot, @Query("access_token") String accessToken);

    @GET(BuildConfig.PATH + "/{vendorId}/meetingroom/bookings/cancel/booking_id={bookingId}&member_id={memberId}")
    Observable<Response<BasicResponse>> cancelMeetingRoom(@Path("vendorId") String vendorId, @Path("bookingId") Long bookingId, @Path("memberId") Long memberId, @Query("access_token") String accessToken);

    //    http://localhost:8888/wreely/services/v2/7/meetingroom/bookings/history/start_date=2018-06-07 06:30:00&end_date=2018-06-07 06:30:00?access_token=97e3ed57cbe89cd1fe673799ccffcdee
    @GET(BuildConfig.PATH + "/{vendorId}/meetingroom/bookings/history/start_date={startDate}&end_date={endDate}")
    Observable<Response<BasicResponse>> meetingHistory(@Path("vendorId") String vendorId, @Path("startDate") String startDate, @Path("endDate") String endDate, @Query("access_token") String accessToken);

    //    http://api.wreely.com/services/v1/events/per_page=1&page_number=1?access_token=qwertyuiopoiuytrewertyuiuytrewertyui
    @GET(BuildConfig.PATH + "/workspaces/nearby/lat={lat}&lon={lng}&rad={radius}")
    Observable<Response<List<NearbyWorkspace>>> getNearbyWorkspaces(@Path("lat") double lat, @Path("lng") double lng, @Path("radius") int radius);

    @GET(BuildConfig.PATH + "/{vendorId}/events/per_page={perPage}&page_number={pageNumber}")
    Observable<Response<BasicResponse>> getVendorEvents(@Path("vendorId") Long vendorId, @Path("perPage") int perPage, @Path("pageNumber") int pageNumber, @Query("access_token") String accessToken);

    //http://api.wreely.com/services/v2/events/attend/event_id=1?access_token=9374243a0cb0818031a051eab4b19d82
    @GET(BuildConfig.PATH + "/events/attend/event_id={eventId}&member_id={memberId}")
    Observable<Response<BasicResponse>> attendEvent(@Path("eventId") int eventId, @Path("memberId") int memberId, @Query("access_token") String accessToken);

    @GET(BuildConfig.PATH + "/events/unattend/event_id={eventId}&member_id={memberId}")
    Observable<Response<BasicResponse>> withdrawEvent(@Path("eventId") int eventId, @Path("memberId") int memberId, @Query("access_token") String accessToken);
//
//    @GET(BuildConfig.PATH + "/case/list")
//    Observable<Response<BasicResponse>> getCases(@Query("access_token") String accessToken);

}
