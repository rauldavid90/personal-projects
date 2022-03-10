package com.example.service;

import com.example.domain.*;
import com.example.repository.exceptions.RepositoryException;
import com.example.utils.PageEvent;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

//import com.itextpdf.text.Paragraph;
//import com.itextpdf.text.Document;
//import com.itextpdf.text.pdf.PdfWriter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class Network {
    UserService userService;
    FriendshipService friendshipService;
    FriendshipRequestService friendshipRequestService;
    ChatRoomService chatRoomService;
    EventService eventService;
    SubscriptionService subscriptionService;

    private int pageNumberFriends = 0;
    private int pageSizeFriends = 5;

    /**
     * constructor
     * @param userService
     * @param friendshipService
     */
    public Network(UserService userService, FriendshipService friendshipService, FriendshipRequestService friendshipRequestService, ChatRoomService chatRoomService, EventService eventService, SubscriptionService subscriptionService) {
        this.userService = userService;
        this.friendshipService = friendshipService;
        this.friendshipRequestService = friendshipRequestService;
        this.chatRoomService = chatRoomService;
        this.eventService = eventService;
        this.subscriptionService = subscriptionService;
    }

    public ChatRoomService getChatRoomService() {
        return chatRoomService;
    }

    public UserService getUserService() {
        return userService;
    }

    public FriendshipService getFriendshipService() {
        return friendshipService;
    }

    public FriendshipRequestService getFriendshipRequestService() {
        return friendshipRequestService;
    }

    public EventService getEventService(){
        return eventService;
    }

    public SubscriptionService getSubscriptionService() {
        return subscriptionService;
    }

    /**
     * Users
     */


    /**
     * adds an user in the list
     * @param firstName
     * @param lastName
     */
    public void addUser(String firstName, String lastName, String username, String password) {
        userService.add(firstName, lastName, username, password);
    }

    /**
     * deletes the friendships that contains the old user
     * updates the user in the list
     * @param id index of user
     * @param firstName new
     * @param lastName new
     */
    public void updateUser(Long id, String firstName, String lastName, String username, String password) {

        for (Friendship p : friendshipService.getAll())
            if (p.getId_friend_1() == id || p.getId_friend_2() == id)
                friendshipService.delete(p.getId());

        userService.update(id, firstName, lastName, username, password);
    }

    /**
     * deletes the friendships that contains him
     * deletes the user
     * @param id deleted user id
     */
    public void deleteUser(Long id) {

        for (Friendship p : friendshipService.getAll())
            if (p.getId_friend_1() == id || p.getId_friend_2() == id)
                friendshipService.delete(p.getId());

        userService.delete(id);
    }

    /**
     * get an user by its id
     * @param id
     * @return
     */
    public User getUserById(Long id){
        return userService.get(id);
    }

    /**
     * get an user by its username
     * @param username
     * @return
     */
    public User getUserByUsername(String username) {
        return userService.getByUsername(username);
    }

    /**
     * gets all the users in the repository
     * converts iterable list to list
     * @return
     */
    public List<User> getAllUsers(){
        List<User> users = StreamSupport.stream(userService.getAll().spliterator(), false)
                .collect(Collectors.toList());
        return users;
    }

    /**
     * prints all users
     */
    public void showAllUsers(){
        userService.showAll();
    }

    /**
     * prints all users with their friends
     * ignores the friendship RepositoryException if there aren't any friendships
     */
    public void showAllUsersWithFriends(){
        for(User u : userService.getAll())
        {
            System.out.print(u);
            System.out.print("Friends: ");
            try{
                for(Friendship p : friendshipService.getAll())
                {
                    if(p.getId_friend_1() == u.getId())
                        System.out.print("(" + p.getId_friend_2() + " " +
                                userService.get(p.getId_friend_2()).getFirstName() + " " +
                                userService.get(p.getId_friend_2()).getLastName() + ") ");
                    if(p.getId_friend_2() == u.getId())
                        System.out.print("(" + p.getId_friend_1() + " " +
                                userService.get(p.getId_friend_1()).getFirstName() + " " +
                                userService.get(p.getId_friend_1()).getLastName() + ") ");
                }
            }
            catch (RepositoryException ex) {
                System.out.print("No friends");
            }
            System.out.println("");
        }
    }


    /**
     * Show the user friends
     * @param id
     */
    public List<User> getFriendsForUser(Long id){
        userService.get(id);
        List<Friendship> friendshipsList=StreamSupport.stream(friendshipService.getAll().spliterator(),false)
                .collect(Collectors.toList());
        List<User> friends = new ArrayList<User>();
        friendshipsList.stream()
                .filter(x->{return x.getId_friend_1()==id || x.getId_friend_2()==id;})
                .forEach((x)->{
                    if(x.getId_friend_1() == id)
                        friends.add(userService.get(x.getId_friend_2()));
                    else
                        friends.add(userService.get(x.getId_friend_1()));
                });
        return friends;
    }

    /**
     * Return current page for friends table
     * @param type, id
     * @return
     * @throws Exception
     */
    public List<User> getPageFriends(PageEvent type, Long id) throws Exception {
        if(type==PageEvent.NEXT_PAGE) pageNumberFriends++;
        if(type==PageEvent.PREVIOUS_PAGE && pageNumberFriends > 0) pageNumberFriends--;
        List<User> lista = getFriendsForUser(id);
        int finalPos = pageNumberFriends * pageSizeFriends + pageSizeFriends;
        if(finalPos > lista.size()) finalPos = lista.size();
        try {
            lista = lista.subList(pageNumberFriends * pageSizeFriends, finalPos);
        }catch (Exception e){
            lista.clear();
        }
        if(lista.isEmpty()){
            pageNumberFriends --;
            throw new Exception("Nu exista alte pagini");
        }
        return lista;
    }

    /**
     * verifies if there is an user with that id
     * prints all friends of an user made during specified month
     * @param id
     * @param month
     */
    public void showFriendsForUserByMonth(Long id, Integer month){
        userService.get(id);

        List<Friendship> friendships = StreamSupport.stream(friendshipService.getAll().spliterator(), false)
                .collect(Collectors.toList());
        friendships.stream()
                .filter(x -> {
                    return x.getId_friend_1().equals(id) || x.getId_friend_2().equals(id);
                })
                .filter(x -> {
                    return  x.getDate().getMonthValue() == month;
                })
                .forEach(x -> {
                            if(x.getId_friend_1() == id) {
                                User friend = userService.get(x.getId_friend_2());
                                System.out.println(friend.getFirstName() + " " + friend.getLastName() + " | " + x.getDate() + " " + x.getTime());
                            }
                            else{
                                User friend = userService.get(x.getId_friend_1());
                                System.out.println(friend.getFirstName() + " " + friend.getLastName() + " | " + x.getDate() + " " + x.getTime());
                            }
                });
    }

    /**
     * verifies the password for an username
     * throws NoSuchElementException if user not found
     * throws ValidatorException if password is incorrect
     * @param username
     * @param password
     */
    public void verifyPassword(String username, String password){
        userService.verifyPassword(username, password);
    }


    /**
     * Friendships
     */


    /**
     * verifies if there are users with those ids
     * adds the friendship to the list of friendships
     * @param id1 id of user 1
     * @param id2 id of user 2
     */
    public void addFriendship(Long id1, Long id2){
        userService.get(id1);
        userService.get(id2);
        friendshipService.add(id1, id2);
    }

    /**
     * verifies if there are users with those ids
     * updates the friendship in the list
     * @param id index of friendship
     * @param id1 new id of user 1
     * @param id2 new id of user 2
     */
    public void updateFriendship(Long id, Long id1, Long id2){
        userService.get(id1);
        userService.get(id2);
        friendshipService.update(id, id1, id2);
    }

    /**
     * removes the friendship from the list
     * @param id index of friendship to be deleted
     */
    public void deleteFriendship(Long id){
       friendshipService.delete(id);
    }

    /**
     * get a friendship by its id
     * @param id
     * @return
     */
    public Friendship getFriendshipById(Long id){
        return friendshipService.get(id);
    }

    /**
     * prints all friendships
     */
    public void showAllFriendships(){
        friendshipService.showAll();
    }


    /**
     * Friendship requests
     */

    /**
     * verifies if there are users with those ids
     * adds the friendship request to the list
     * @param from_id
     * @param to_id
     * @param status
     */
    public void addFriendshipRequest(Long from_id, Long to_id, String status) {
        User userFrom = userService.get(from_id);
        User userTo = userService.get(to_id);
        friendshipRequestService.add(from_id, to_id,userTo.getUsername(),userFrom.getUsername(), status);
    }

    /**
     * removes the friendship request from the list
     * @param id index of friendship request to be deleted
     */
    public void deleteFriendshipRequest(Long id){
        friendshipRequestService.delete(id);
    }

    /**
     * prints all the friendship requests
     */
    public void showAllFriendshipRequests(){
        friendshipRequestService.showAll();
    }

    /**
     * print the requests received by an user with the given id
     * @param id
     */
    public List<FriendshipRequest> getAllFriendshipRequestsReceivedByUserId(Long id){
        List<FriendshipRequest> requests = StreamSupport.stream(friendshipRequestService.getAll().spliterator(), false)
                .collect(Collectors.toList());
        List<FriendshipRequest> requests_for_id = new ArrayList<>();
        requests.stream()
                .filter(x -> {
                    return x.getTo_id() == id;
                })
                .forEach(x -> {
                    requests_for_id.add(x);
                });
        return requests_for_id;
    }

    /**
     * print the requests received by an user with the given id
     * @param id
     */
    public List<FriendshipRequest> getAllFriendshipRequestsSentByUserId(Long id){
        List<FriendshipRequest> requests = StreamSupport.stream(friendshipRequestService.getAll().spliterator(), false)
                .collect(Collectors.toList());
        List<FriendshipRequest> requests_for_id = new ArrayList<>();
        requests.stream()
                .filter(x -> {
                    return x.getFrom_id() == id;
                })
                .forEach(x -> {
                    requests_for_id.add(x);
                });
        return requests_for_id;
    }

    /**
     * verifies if there is a friendship request with that id
     * approves a friendship requests
     * adds an friendship between the users
     * @param id
     */
    public void approveFriendshipRequest(Long id){
        friendshipRequestService.get(id);
        friendshipRequestService.approve(id);
        FriendshipRequest approvedRequest = friendshipRequestService.get(id);
        friendshipService.add(approvedRequest.getFrom_id(), approvedRequest.getTo_id());
    }

    /**
     * verifies if there is a friendship request with that id
     * rejects a friendship request
     * @param id
     */
    public void rejectFriendshipRequest(Long id){
        friendshipRequestService.get(id);
        friendshipRequestService.reject(id);
    }

    /**
     * Delete the friendship between two users
     * @param id1 Id of user 1
     * @param id2 Id of user 2
     */
    public void deleteFriendshipforUsers(Long id1,Long id2){
        List<Friendship> friendships = StreamSupport.stream(friendshipService.getAll().spliterator(), false)
                .collect(Collectors.toList());
        friendships.forEach(x ->{
            Long xid1=x.getId_friend_1();
            Long xid2=x.getId_friend_2();
            if ((xid1==id1 && xid2==id2) || (xid1==id2 && xid2==id1)){
                deleteFriendship(x.getId());
            }
        });
    }

    /**
     * Returns all the users which are not friends with specified user
     * @param userId
     * @return
     */
    public List<User> getAllUserWhichAreNotFriendsWithUser(Long userId){
        User curentUser=userService.get(userId);
        List<User> userList=StreamSupport.stream(userService.getAll().spliterator(),false)
                .collect(Collectors.toList());
        userList.remove(curentUser);
        try {
            List<User> friendList = getFriendsForUser(userId);
            friendList.forEach(x -> {
                userList.remove(x);
            });
        }catch (Exception e){}
        return userList;
    }


    /**
     * Chatroom
     */


    /**
     *
     * @param logged
     * @param user
     * @param startDate
     * @param endDate
     * @param saveLocation
     */
    public String createChatHistoryReport(User logged,User user,LocalDateTime startDate, LocalDateTime endDate, String saveLocation) throws Exception {
        ChatRoom chatroom = chatRoomService.findChatRoomForUsers(logged,user);
        List<Message> messages = new ArrayList<>();
        if(chatroom != null) messages = chatRoomService.getMessagesReceived(chatroom,logged,startDate,endDate);

        PDDocument document = new PDDocument();
        PDPage pdPage = new PDPage();
        document.addPage(pdPage);
        PDPageContentStream contentStream = new PDPageContentStream(document,pdPage);

        contentStream.beginText();
        contentStream.setFont(PDType1Font.TIMES_ROMAN,14);
        contentStream.setLeading(14.5f);
        contentStream.newLineAtOffset(250,710);
        String string = "Raport - statistica mesaje\n";
        contentStream.showText("Raport - statistica mesaje");
        contentStream.endText();
        contentStream.beginText();
        contentStream.newLineAtOffset(50,650);
        contentStream.setFont(PDType1Font.TIMES_ROMAN,12);
        contentStream.showText(logged.getLastName()+" "+logged.getFirstName()+" a primit "+ messages.size()+" mesaje de la "
          + user.getFirstName() +" "+user.getLastName()+" in perioada: de la "+startDate.toLocalDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))+" pana la "+endDate.toLocalDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        contentStream.newLine();
        string+=logged.getLastName()+" "+logged.getFirstName()+" a primit "+ messages.size()+" mesaje de la "
                + user.getFirstName() +" "+user.getLastName()+" in perioada: de la "+startDate.toLocalDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))+" pana la "+endDate.toLocalDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))+"\n";
        contentStream.showText("Mesaje: ");
        string+="Mesaje: \n";
        contentStream.newLine();
        for(Message message:messages){
            contentStream.showText(message.getMessage()+ " la data: " + message.getDate().toLocalDate()+" ora: " + message.getDate().toLocalTime());
            string+=message.getMessage()+ " la data: " + message.getDate().toLocalDate()+" ora: " + message.getDate().toLocalTime()+"\n";
            contentStream.newLine();
        }
        contentStream.endText();
        contentStream.close();
        document.save(saveLocation);
        document.close();
        return string;
    }

    /**
     *
     * @param logged
     * @param startDate
     * @param endDate
     * @param saveLocation
     */
    public String createHistoryReport(User logged,LocalDateTime startDate, LocalDateTime endDate, String saveLocation) throws Exception {
        List<ChatRoom> lista = chatRoomService.getChatroomsForUser(logged);
        List<Message> mesaje = new ArrayList<>();
        for (ChatRoom chatRoom : lista) {
            chatRoomService.getAllMessagesForChatRoom(chatRoom).stream()
                    .filter(x -> !x.getFrom().equals(logged))
                    .filter(x->x.getDate().isAfter(startDate) && x.getDate().isBefore(endDate))
                    .forEach(x -> mesaje.add(x));
        }
        List<Friendship> prieteni = StreamSupport.stream(friendshipService.getAll().spliterator(), false)
                .filter(x -> x.getId_friend_1() == logged.getId() || x.getId_friend_2() == logged.getId())
                .filter(x -> x.getDate().isAfter(startDate.toLocalDate()) && x.getDate().isBefore(endDate.toLocalDate())).collect(Collectors.toList());

        PDDocument document = new PDDocument();
        PDPage pdPage = new PDPage();
        document.addPage(pdPage);
        PDPageContentStream contentStream = new PDPageContentStream(document,pdPage);
        String string = "Raport - statistica mesaje si prieteni\n";
        contentStream.beginText();
        contentStream.setFont(PDType1Font.TIMES_ROMAN,14);
        contentStream.setLeading(14.5f);
        contentStream.newLineAtOffset(250,710);
        contentStream.showText("Raport - statistica mesaje si prieteni");
        contentStream.endText();
        contentStream.beginText();
        contentStream.newLineAtOffset(50,650);
        contentStream.setFont(PDType1Font.TIMES_ROMAN,12);
        contentStream.showText(logged.getLastName() + " " + logged.getFirstName() + " a primit " + mesaje.size() + " mesaje si a legat "
                + prieteni.size()+" in perioada: de la "+startDate.toLocalDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))+" pana la "+endDate.toLocalDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        contentStream.newLine();
        string+=logged.getLastName() + " " + logged.getFirstName() + " a primit " + mesaje.size() + " mesaje si a legat "
                + prieteni.size()+" in perioada: de la "+startDate.toLocalDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))+" pana la "+endDate.toLocalDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))+"\n";
        if(!prieteni.isEmpty()) {
            String prieteniPdf = "Prieteni noi: ";
            for (Friendship friendship : prieteni) {
                User p = getUserById(friendship.getId_friend_1());
                if (logged.equals(p)) p = getUserById(friendship.getId_friend_2());
                prieteniPdf += p.getLastName() + " " + p.getFirstName() + ", ";
            }
            prieteniPdf = prieteniPdf.substring(0, prieteniPdf.length() - 2);
            prieteniPdf += ".";
            contentStream.showText(prieteniPdf);
            string+=prieteniPdf;
        }
        contentStream.endText();
        contentStream.close();
        document.save(saveLocation);
        document.close();
        return string;
    }


    /**
     * Events
     */


    /**
     * adds an event
     * @param name
     * @param description
     * @param date
     * @param time
     */
    public void addEvent(String name, String description, LocalDate date, LocalTime time) {
        eventService.add(name, description, date, time);
    }

    /**
     * get an event by id
     * @param id
     * @return
     */
    public Event getEventById(Long id) {
        return eventService.get(id);
    }

    /**
     * get all events
     * @return
     */
    public List<Event> getAllEvents(){
        List<Event> events = StreamSupport.stream(eventService.getAll().spliterator(), false)
                .collect(Collectors.toList());
        return events;
    }

    /**
     * @return nextPage of Event elements
     */

    public List<Event> getNextPage() throws Exception {
        return eventService.getPage(PageEvent.NEXT_PAGE);
    }

    public List<Event> getPreviousPage() throws Exception {
        return  eventService.getPage(PageEvent.PREVIOUS_PAGE);
    }

    public List<Event> getCurrentPage() throws Exception {
        return eventService.getPage(PageEvent.CURRENT_PAGE);
    }

    /**
     * Subscriptions
     */

    /**
     * add a subscription
     * @param user_id
     * @param event_id
     * @param notificationsStatus
     */
    public void addSubscription(Long user_id, Long event_id, String event_name, String notificationsStatus){
        subscriptionService.add(user_id, event_id, event_name, notificationsStatus);
    }

    /**
     * delete a subscription
     * @param id
     */
    public void deleteSubscription(Long id){
        subscriptionService.delete(id);
    }

    /**
     * notifications swtich
     * @param id
     */
    public void notificationsSwitch(Long id){
        subscriptionService.notificationsSwitch(id);
    }

    /**
     * get all subscriptions
     * @return
     */
    public List<Subscription> getAllSubscriptions(){
        List<Subscription> subscriptions = StreamSupport.stream(subscriptionService.getAll().spliterator(), false)
                .collect(Collectors.toList());
        return subscriptions;
    }

    /**
     * gets all subscriptions for an user by its id
     * @param id
     * @return
     */
    public List<Subscription> getAllSubscriptionsForUserId(Long id){
        List<Subscription> subscriptions = StreamSupport.stream(subscriptionService.getAll().spliterator(), false)
                .collect(Collectors.toList());
        List<Subscription> subscriptions_for_id = new ArrayList<>();
        subscriptions.stream()
                .filter(x -> {
                    return x.getUser_id() == id;
                })
                .forEach(x -> {
                    subscriptions_for_id.add(x);
                });
        return subscriptions_for_id;
    }
}
