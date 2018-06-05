package com.celerstudio.wreelysocial.network;

import com.celerstudio.wreelysocial.BuildConfig;
import com.celerstudio.wreelysocial.models.BasicResponse;
import com.celerstudio.wreelysocial.models.MeetingRoom;
import com.celerstudio.wreelysocial.models.MeetingRoomSlot;
import com.celerstudio.wreelysocial.models.User;

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

    @GET(BuildConfig.PATH + "/companies")
    Observable<Response<BasicResponse>> getCompanies(@Query("access_token") String accessToken);

    @GET(BuildConfig.PATH + "/members")
    Observable<Response<BasicResponse>> getVendorMembers(@Query("access_token") String accessToken);

    @GET(BuildConfig.PATH + "/{companyId}/members")
    Observable<Response<BasicResponse>> getCompanyMembers(@Path("companyId") String companyId, @Query("access_token") String accessToken);

    @GET(BuildConfig.PATH + "/meetingrooms")
    Observable<Response<BasicResponse>> getMeetingRooms(@Query("access_token") String accessToken);

    @GET(BuildConfig.PATH + "/meetingroom/bookings/room_id={roomId}&start_date={startDate} 00:00:00&end_date={endDate} 23:59:59")
    Observable<Response<BasicResponse>> getMeetingRoomSlots(@Path("roomId") int roomId, @Path("startDate") String startDate, @Path("endDate") String endDate, @Query("access_token") String accessToken);

    @POST(BuildConfig.PATH + "/meetingroom/bookings")
    Observable<Response<BasicResponse>> bookMeetingRoom(@Body MeetingRoomSlot slot, @Query("access_token") String accessToken);

    @GET(BuildConfig.PATH + "/meetingroom/bookings/cancel/booking_id={bookingId}&member_id={memberId}")
    Observable<Response<BasicResponse>> cancelMeetingRoom(@Path("bookingId") Long bookingId, @Path("memberId") Long memberId, @Query("access_token") String accessToken);

    @GET(BuildConfig.PATH + "/meetingroom/bookings/history/member_id={memberId}&start_date={startDate}&end_date={endDate}")
    Observable<Response<BasicResponse>> meetingHistory(@Path("memberId") Long memberId, @Path("startDate") String startDate, @Path("endDate") String endDate, @Query("access_token") String accessToken);
//
//    @GET(BuildConfig.PATH + "/case/list")
//    Observable<Response<BasicResponse>> getCases(@Query("access_token") String accessToken);

}
