package com.merpay.MessageQ.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import com.merpay.MessageQ.Exception.NoSubscribersToTopicException;
import com.merpay.MessageQ.Exception.TopicUnsubscribedException;
import com.merpay.MessageQ.Exception.UnauthorisedToPublishException;
import com.merpay.MessageQ.Exception.UnregisteredTopicException;
import com.merpay.MessageQ.model.Topic;
import com.merpay.MessageQ.model.Subscriber;

import com.merpay.MessageQ.model.Message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * The Message Queue Controller Class.
 * A REST API. Handles REST based requests from other services and responds
 */
@RestController
public class MQController {

    // Logger Class
    private Logger logger = LoggerFactory.getLogger(MQController.class);

    // This Map stores a Topic and a list of Publishers associated with that Topic
    private static Map<String, List<String>> topicPublisherMap = new HashMap<>();

    // Stores the Topic and the List of messages associated with it. The Messages
    // list is stored in the form of a Queue.
    private static Map<String, Queue<Message>> topicMessageMap = new HashMap<>();

    // Stores the Topic and the list of subscribers that have subscribed to
    // that particular topic.
    // Set has been used to eliminate the possibility of duplicate subscribers
    private static Map<String, Set<Subscriber>> topicSubscriberMap = new HashMap<>();

    // This Map stores the subscribers and their Message Queues.
    private static Map<String, Queue<Message>> subscriberMessageMap = new HashMap<>();

    /**
     * Registers a Topic
     * 
     * @param topic Topic
     */
    @PostMapping("/topic/register")
    public void registerTopic(@RequestBody Topic topic) {

        logger.info("Entered Register Topic Method");

        if (topicPublisherMap.containsKey(topic.getTopicId())) {

            List<String> list = topicPublisherMap.get(topic.getTopicId());
            list.add(topic.getPublisherId());
            topicPublisherMap.put(topic.getTopicId(), list);

        } else {

            List<String> list = new ArrayList<>();
            list.add(topic.getPublisherId());

            topicPublisherMap.put(topic.getTopicId(), list);
        }
        logger.info("Exiting Register Topic Method");

    }

    /**
     * Publishes a Message
     * 
     * @param message     The Message to be published
     * @param publisherId The Unique Id of the Publisher
     * @param topicId     The Topic Id
     * @throws TopicUnsubscribedException Throws this exception when one tries to
     *                                    publish a message to an unregistered topic
     * 
     */
    @PostMapping("/publish")
    public void publishMessage(@RequestBody Message message, @RequestParam String publisherId,
            @RequestParam String topicId) {

        logger.info("Entered Publish Message Method");

        if (!topicPublisherMap.containsKey(topicId)) {

            throw new UnregisteredTopicException(
                    "Nobody registered " + topicId + " topic yet. So, unauthorised to publish");

        } else {
            if (!topicPublisherMap.get(topicId).contains(publisherId)) {
                throw new UnauthorisedToPublishException(
                        "Publisher " + publisherId
                                + " is unauthorised to publish to this topic. This publisher not registered with topic "
                                + topicId);
            } else {

                if (topicMessageMap.containsKey(topicId)) {

                    Queue<Message> queue = topicMessageMap.get(topicId);

                    queue.offer(message);
                    topicMessageMap.put(topicId, queue);
                } else {
                    Queue<Message> queue = new LinkedList<>();
                    queue.offer(message);
                    topicMessageMap.put(topicId, queue);
                }
                sendFreshMessagesToSubscribers(topicId, message);

            }
        }
        logger.info("Exiting Publish Message Method");
    }

    /**
     * Method that allows a subscriber to subscribe to a topic
     * 
     * @param topicId      Topic Id to be subscribed to
     * @param subscriberId Unique Id of the subscriber
     */
    @PostMapping("/subscribe/topic")
    public void subscribeTopic(@RequestParam String topicId, @RequestParam String subscriberId) {

        logger.info("Entered Subscribe Topic method");

        if (topicSubscriberMap.containsKey(topicId)) {

            Set<Subscriber> set = topicSubscriberMap.get(topicId);
            Subscriber subscriber = new Subscriber(subscriberId);
            set.add(subscriber);
            topicSubscriberMap.put(topicId, set);
            if (!subscriberMessageMap.containsKey(subscriberId)) {

                subscriberMessageMap.put(subscriberId, new LinkedList<Message>());

            }
            sendMessagesWhenNewSubscriberComes(topicId, subscriberId);

        } else {

            Set<Subscriber> set = new HashSet<>();
            Subscriber subscriber = new Subscriber(subscriberId);
            set.add(subscriber);
            topicSubscriberMap.put(topicId, set);
            if (!subscriberMessageMap.containsKey(subscriberId)) {

                subscriberMessageMap.put(subscriberId, new LinkedList<Message>());

            }
            sendMessagesWhenNewSubscriberComes(topicId, subscriberId);

            logger.info("Exiting Subscribe Topic Method");
        }

    }

    /**
     * Gets the Message from a topic. Called by Subscribers
     * 
     * @param topicId      The uinque Id of the topic
     * @param subscriberId The Subscriber's Id
     * @return Returns the message to the subscriber
     */
    @GetMapping("/message/get")
    public String getMessageFromTopic(@RequestParam String topicId, @RequestParam String subscriberId) {

        logger.info("Entered Get Message From Topic Method");

        Set<Subscriber> set = topicSubscriberMap.get(topicId);

        if (set == null) {
            throw new NoSubscribersToTopicException("There are no subscribers to topic " + topicId);
        }
        boolean has = false;

        for (Subscriber subscriber : set) {

            if (subscriber.getSubscriberId().equals(subscriberId)) {
                has = true;

            }
        }

        if (has) {
            Queue<Message> queue = subscriberMessageMap.get(subscriberId);

            if (queue.isEmpty()) {
                return "No New Message";
            }

            return queue.peek().getMessage();

        } else {
            throw new TopicUnsubscribedException(
                    "Subscriber " + subscriberId + " did not subscribe to topic " + topicId);

        }
    }

    /**
     * Acknowledges the message.
     * 
     * @param topicId      Topic ID
     * @param subscriberId Subscriber's Id
     */
    @GetMapping("/message/ack")
    public void acknowledgeMessage(@RequestParam String topicId, @RequestParam String subscriberId) {

        logger.info("Entered ack Message method");
        Set<Subscriber> set = topicSubscriberMap.get(topicId);

        boolean has = false;

        for (Subscriber subscriber : set) {

            if (subscriber.getSubscriberId().equals(subscriberId)) {
                has = true;

            }
        }

        if (has) {

            Queue<Message> queue = subscriberMessageMap.get(subscriberId);

            queue.remove();
        } else {

            throw new TopicUnsubscribedException(
                    "Subscriber " + subscriberId + " did not subscribe to topic " + topicId);
        }
        logger.info("Exiting Ack Message method");

    }

    /**
     * Sends Newly Arrived Messages to all the Subscribers of that topic
     * 
     * @param topicId Unique Id of the topic
     * @param message The arrived message
     */
    private void sendFreshMessagesToSubscribers(String topicId, Message message) {

        logger.info("Entered Send Fresh Messages to Subscribers Method");

        Set<Subscriber> subscribersOfTopic = topicSubscriberMap.get(topicId);
        Queue<Message> queue = topicMessageMap.get(topicId);
        Queue<Message> subscriberQueue;

        if (subscribersOfTopic == null || subscribersOfTopic.isEmpty()) {

            return;
        }

        while (!queue.isEmpty()) {

            for (Subscriber subscriber : subscribersOfTopic) {
                subscriberQueue = subscriberMessageMap.get(subscriber.getSubscriberId());
                subscriberQueue.add(queue.peek());

                subscriberMessageMap.put(subscriber.getSubscriberId(), subscriberQueue);

            }
            queue.remove();
        }
        topicMessageMap.put(topicId, queue);
        logger.info("Exiting Send Fresh Messages to Subscribers Method");

    }

    /**
     * Sends Messages from the Common Message Queue to the subscribers who have
     * subscribed to that topic
     * 
     * @param topicId
     * @param subscriberId
     */
    private void sendMessagesWhenNewSubscriberComes(String topicId, String subscriberId) {

        logger.info("Entering Send Messages When New Subscriber Comes method");

        Queue<Message> queue = topicMessageMap.get(topicId);
        Queue<Message> subscriberQueue = subscriberMessageMap.get(subscriberId);

        if (queue == null) {
            return;
        }

        while (!queue.isEmpty()) {

            subscriberQueue.add(queue.remove());

        }
        subscriberMessageMap.put(subscriberId, subscriberQueue);
        logger.info("Exiting Send Messages When New Subscriber Comes method");

    }

}
