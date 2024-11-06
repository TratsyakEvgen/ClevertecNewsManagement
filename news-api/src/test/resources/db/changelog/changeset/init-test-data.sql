CREATE TABLE news
(
    news_id  BIGSERIAL PRIMARY KEY,
    username TEXT      NOT NULL,
    date     TIMESTAMP NOT NULL,
    title    TEXT      NOT NULL,
    text     TEXT      NOT NULL,
    text_tsv tsvector GENERATED ALWAYS AS (to_tsvector('english',
                                                       coalesce(username, '') || ' ' || coalesce(title, '') || ' ' ||
                                                       coalesce(text, ''))) STORED
);

CREATE TABLE comments
(
    comment_id BIGSERIAL PRIMARY KEY,
    username   TEXT      NOT NULL,
    date       TIMESTAMP NOT NULL,
    text       TEXT      NOT NULL,
    news_id    BIGINT    NOT NULL,
    text_tsv   tsvector GENERATED ALWAYS AS (to_tsvector('english',
                                                         coalesce(username, '') || ' ' ||
                                                         coalesce(text, ''))) STORED
);

ALTER TABLE comments
    ADD CONSTRAINT fk_comments_news FOREIGN KEY (news_id) REFERENCES news (news_id);

CREATE INDEX comment_text_search_idx ON comments USING GIN (text_tsv);

CREATE INDEX news_text_search_idx ON news USING GIN (text_tsv);

INSERT INTO news(username, date, title, text)
VALUES ('journalist1', current_timestamp, 'UN’s Guterres arrives in Russia for controversial BRICS summit',
        'The decision by the Portuguese UN Secretary General António Guterres to attend a summit hosted by Vladimir Putin has met with criticism from Ukraine and its allies.The UN Secretary General has arrived in Kazan, Russia, to attend a controversial summit of developing countries including China and Iran.  His decision to attend the meeting hosted by Russian President Vladimir Putin has met with criticism from Ukraine and its allies, given the ongoing conflict following the 2022 full-scale invasion.  “This is a wrong choice that does not advance the cause of peace. It only damages the UN''s reputation,” the Kyiv foreign affairs ministry posted on X on Monday based on reports of his attendance, noting that he had declined to attend a peace summit in Switzerland to which Ukraine invited him earlier this year.  The foreign minister of Lithuania, a noted Russia hawk, has also branded Guterres’ involvement as “unacceptable”.'),
       ('journalist1', current_timestamp,
        'Back from Silicon Valley, this engineer explains us how to access all the paid channels and platforms for FREE',
        'While some people spend up to $100 to subscribes in all come out TV subscription and streaming platforms, a little genius has found a way that allow everyone to legally access all the channels from our TVs for free (regardless of the TV model and year). We had the chance to interview him exclusively at Techno Mag when he returned from Silicon Valley. In this article you''ll find out how to access all the TV channels, no matter how old your TV is. Who is Rémy, the Swiss genius who invented a box capable of unlocking access to all TV channels? Rémy is the kind of guy like Mark Zuckerberg, Bill Gates or Elon Musk. Graduating as an engineer two years ahead of his classmates, he leaves early in 2020 for Silicon Valley to create the next startup that will revolutionize his industry. Once there, he quickly teamed up with two other talented engineers and set out to invent a modern and conected television for less than $100. But soon enough, the young team realised that the project was doomed to failure: the price of electronic parts making it impossible for the project to be economically viable. After this failure, Rémy went back to the drawing board and came up with an idea that will revolution the entertainment industry - a box that can be plugged into any TV and give users access to all TV channels and video-on-demand platforms. Offering a seamless viewing experience and eliminates the need for multiple subscriptions.'),
       ('journalist1', current_timestamp, 'Lebanon’s Middle East Airlines keeps flying despite conflict',
        'MEA, the only airline still flying from Beirut, conducts daily risk assessments to ensure safety and has received assurances from Israel that civilian operations won’t be targeted. Since Israel began its offensive against Hezbollah in Beirut’s southern suburbs, Lebanon’s national carrier, Middle East Airlines (MEA), has continued flying. Located on the coast, where many Hezbollah operations take place, Beirut’s airport remains operational, unlike during the 2006 war when Israeli strikes quickly disabled the facility. Captain Mohammed Aziz, an adviser to MEA chairman Mohamed El-Hout, revealed that the airline had received assurances from Israel that neither the airport nor its planes would be targeted, provided they serve only civilian purposes. Daily assessments are conducted to ensure the safety of operations. “As long as you see us operating, it means our threat assessment says we can operate,” Aziz stated, emphasising that MEA would never endanger lives.'),
       ('journalist1', current_timestamp, 'Roman Polanski no longer faces trial over alleged 1973 rape of minor',
        'Oscar-winning director Roman Polanski has reached a settlement in a civil case over a 1973 sexual assault allegation. The case, scheduled for trial in Los Angeles next August, has now been withdrawn. French-Polish director Roman Polanski, who fled the US in the late 70s after admitting to statutory rape of a 13-year-old, has reached a settlement in the civil case over an alleged sexual assault of another minor. He will no longer face trial, which was scheduled for August 2025. The case against the director of Rosemary’s Baby and The Pianist, which concerned an alleged sexual attack in 1973, had been due in civil court in Los Angeles next August. The suit, filed in June 2023, claimed Polanski took a then-teenager - named anonymously in filings as Jane Doe - to dinner at a restaurant in Los Angeles in 1973. He allegedly gave her tequila, and when she began to feel dizzy, drove her to his home, where he forced himself on her. Alexander Rufus-Isaacs, Polanski''s US attorney, told AFP that the case was "settled in the summer to the parties'' mutual satisfaction and has now been formally dismissed.”'),
       ('journalist1', current_timestamp,
        'Russia and Iran may fuel violent protests after US elections, intelligence officials say',
        'The warning from two senior intelligence officials comes after an "unwitting" US citizen was recruited by Russian military intelligence to organise protests in the US in January. Russia and Iran may try to encourage violent protests online in the US after the country''s presidential election in November, senior intelligence officials warned Tuesday. According to a declassified national intelligence memo, the aim would be to increase division in US society and cast doubt on the election results, potentially complicating the smooth transfer of power. The memo''s authors warned that Russia and Iran could organise protest events themselves or encourage citizens to participate in protests, usually through online channels such as social media. The warning comes after Russian military intelligence tried to recruit a US citizen to organise protests in the US in January. The American was thought to be "probably unwitting" and unaware that he was in contact with Russian agents.'),
       ('journalist2', current_timestamp, 'Heat pump makers seek help from Brussels amid sales slump ',
        'Heat pumps are set to play at least as important a role in Europe’s energy transition as electric cars, but a drop in demand following record sales during the 2022 energy crisis has manufacturers looking to Brussels to address what they see as a taxes and subsidies that favour gas. Reports out this week suggest governments and the EU are doing too little to promote the replacement of fossil fuel-fired heating systems with heat pumps, with support schemes of variable quality and gas prices artificially low compared to electricity. According to the European Heat Pump Association (EHPA), sales fell 47% in the first half of the year compared to the same period in 2023, with only Scandinavia – where the technology is already mainstream – bucking the trend. After analysing the relative price of electricity and gas, the Brussels-based industry lobby found that all-time record sales in 2022 coincided with a period when most of Europe saw the unit cost of electricity at less than 2.5 times that of gas. EHPA noted that heat pumps – which act like refrigerators in reverse, drawing energy from the air or ground and transferring it to radiators in the home – are cheaper overall than gas heating even when the electricity price is around double that of gas.'),
       ('journalist2', current_timestamp,
        'Which European universities and countries file the most patent applications?',
        'A new study shows a growing contribution of universities to the continent''s innovation ecosystem but laments the bloc''s fragmentation is crippling the ability to turn high-level scientific research into economic competitiveness. A new report by the European Patent Observatory (EPO) shows that patent submissions across the continent are concentrated in just a few places. Half of all direct and indirect patent applications submitted with the European Patent Office between 2000 and 2020 - on a total of over 100,000 - came from only 5% of all European universities. France''s University of Grenoble was the most active, with 3,348 submissions, followed by Zurich''s ETH (2,219) and Munich''s Technical University (2,183). Overall, submissions were the highest in Europe''s most industrialised regions, says the report, "where opportunities for collaboration and technology transfers with industry are greatest."'),
       ('journalist2', current_timestamp,
        'ABBA, The Cure and Radiohead musicians sign statement against using creatives’ work to train AI',
        'Among the 13,500 signatories protesting the use of creatives’ work to train aritificial intelligence tools are composer Max Richter, writer Kazuo Ishiguro, as well as actors Julianne Moore and Kevin Bacon. Thousands of artists, including musicians from ABBA, The Cure and Radiohead, have signed a protest letter against using creatives’ work to train aritificial intelligence tools. Musicians, actors and authors signed the letter warning against the mining of their artistry, in what is the latest outcry about AI tools that can spit out synthetic images, music and writings after being trained on huge troves of human-made works. “The unlicensed use of creative works for training generative AI is a major, unjust threat to the livelihoods of the people behind those works, and must not be permitted,” says the statement. Among the 13,500 signatories are Björn Ulvaeus of ABBA, The Cure''s Robert Smith, Thom Yorke and his Radiohead bandmates, and composers John Rutter and Max Richter.'),
       ('journalist2', current_timestamp, 'What key factors could keep the euro under pressure for some time?',
        'The euro extended its losses against other major currencies within the G-10 group, with the EUR/USD pair dropping below the pivotal 1.08 level on Tuesday. The single currency may remain weak for some time. The euro continued its decline against other major currencies following the IMF''s downgrade of the eurozone''s economic growth outlook. Over the past month, the single currency has fallen more than 3% against the US dollar, dropping below 1.08 - the lowest level since 2 August. The euro has also weakened against the British pound, Swiss franc, and Australian dollar by 0.77%, 1.47%, and 1.54%, respectively, during the same period. Below-target headline inflation, ongoing economic weakness, and political uncertainties have all contributed to a soft euro. Typically, the upcoming US election plays a significant role in driving currency market trends.'),
       ('journalist2', current_timestamp, 'Israeli army says it killed top Hezbollah leader in Beirut airstrike',
        'Hashem Safieddine, a powerful cleric within the party ranks, was expected to succeed Hassan Nasrallah, who was killed in a different Israeli airstrike in September. The Israeli military said it killed Hashem Safieddine, a top Hezbollah official widely expected to become the group''s next leader, in a strike on Tuesday. According to them, he was killed earlier this month in an airstrike in southern Beirut not long after the killing of Hezbollah''s former leader, Hassan Nasrallah. There was no immediate confirmation from the militant group about Safieddine''s fate. The strike that killed Saffieddine also killed 25 other Hezbollah leaders, according to Israel, whose airstrikes in southern Lebanon in recent months have killed many of Hezbollah’s top echelon.'),
       ('journalist3', current_timestamp,
        'Trump accuses UK PM''s Labour Party of ''foreign interference'' in US election',
        'Presidential candidate Donald Trump filed a legal complaint against British Prime Minister Keir Starmer''s Labour Party, accusing it of meddling in the election in a document that cited the American revolution and misspelled ''Britain.'' Trump launched a legal complaint overnight against the UK''s ruling Labour party and the Harris-Walz campaign, accusing the party of "interference" in the US election after Labour officials allegedly travelled to the US offering advice to Democratic candidates. The letter opened citing the American revolution before spelling "Britain" as "Britian" when mentioning the surrender of British forces to the US at the Battle of Yorktown in 1781. "It appears that the Labour Party and the Harris for President campaign have forgotten the message" the complaint said, without explaining the comparison. It added that contact between Labour officials and the Harris campaign amount to "illegal foreign contributions" and called for an immediate investigation.'),
       ('journalist3', current_timestamp,
        'Russia bans imports of agro-products from Kazakhstan after refusal to join BRICS',
        'Kazakhstan is observing the BRICS summit with interest but has not decided on joining. Meanwhile, Russia restricts agricultural imports from Kazakhstan, citing safety concerns. On the eve of the BRICS summit, Kazakhstan said that it was still "watching the evolution" of the bloc with interest. Despite Moscow''s approaches to President Kasym-Jomart Tokayev, the question of joining the association is not yet on the table, as Tokayev said that in the current circumstances, there was no alternative to the UN. Immediately afterwards, it became known that Russia was restricting the importing of agricultural products from Kazakhstan, including fresh flowers, due to an alleged invasion of pest insects. "Kazakhstan re-exports most flowers from the Netherlands, Poland and Belgium. However, the competent agencies of these countries do not provide the appropriate level of control over the safety of products," Russian phytosanitary agency Rosselkhoznadzor said in a statement.'),
       ('journalist3', current_timestamp, 'What is immunity debt – and is it really making kids sick?',
        'There’s been an uptick in viral infections in the post-pandemic period, but that isn’t necessarily a bad thing. The COVID-19 pandemic may be in the rearview mirror, but Europe is still grappling with its aftereffects. In Denmark, for example, the number of children and teenagers with mycoplasma pneumoniae – a bacteria that causes respiratory tract infections – surged about three-fold in the 2023-2024 season compared to the pre-pandemic years. Hospitalisations were also 2.6 times higher. However, the Danish kids’ infections were no more severe than in previous years – indicating that while more people got sick, they didn’t get sicker than expected. Countries like England, Germany, and France have also seen unusual upticks in infections such as respiratory syncytial virus (RSV) in recent years.'),
       ('journalist3', current_timestamp, 'Is Russia behind recent arson attacks in Europe?',
        'Incidents in Germany, Latvia, Lithuania, Poland and the UK have led to suspicions that Moscow is trying to sabotage European countries for backing Ukraine against the Russian invasion. British counter-terrorism police are looking into whether Russian spies planted a device on a UK-bound plane which later caught fire at a warehouse in Birmingham in July. The Guardian first reported the news, saying that the parcel is believed to have arrived at the DHL warehouse by air. It’s not known where it was heading. The paper added that no one was reported as injured in the fire and that the local fire brigade and staff handled the blaze. It follows similar incidents in other European countries in recent months.'),
       ('journalist3', current_timestamp, 'Russia’s expanded BRICS meeting sends jitters around Europe',
        'China’s Xi Jinping, India’s Narendra Modi, Turkey’s Recep Tayyip Erdoğan and possibly even the UN Secretary General are attending a controversial meeting in Russia of a grouping that threatens to dwarf the EU’s economic clout. An expanded meeting of emerging economies taking place in Kazan, Russia, is sending jitters among EU policymakers. The club formed in 2009 now includes not just Brazil, China, India, Russia and South Africa – original members whose acronym gave it the name BRICS, and whose leaders Xi Jinping, Narendra Modi and Vladimir Putin are set to meet over the coming days. As of 1 January, Egypt, Ethiopia, Iran, Saudi Arabia and the United Arab Emirates also joined the club – creating a bloc worth over 37% of global GDP – and one potentially at odds with other institutions such as the G7 and NATO. But the threat to the existing US-led hegemony isn’t immediate, the Carnegie Endowment’s Stewart Patrick has argued.'),
       ('journalist4', current_timestamp, 'EU new car registrations for September continue to see a slide',
        'EU new car registrations in September were mainly dampened by poor figures from France, Italy and Germany. Once again, Spain bucked the trend. New car registrations within the European Union fell -6.1% in September mainly dragged down by Italy, France and Germany, according to the most recent data from the Association of European Automobile Manufacturers (ACEA). Italy faced a -10.7% decline in new car registrations in September, whereas France experienced a -11.1% drop. Germany also followed this trend, with a 7% fall in new car registrations last month. However, Spain bucked the trend, with new car registrations growing 6.3% in September. Similarly, Germany also experienced a -1% decline in new car registrations for the first nine months of the year, along with France, where they dropped -1.8%. On the other hand, Italian new car registrations rose 2.1% for this period, along with Spain’s, at 4.7%. In September 2024, 29.8% of the EU car market was taken up by petrol cars, whereas diesel cars made up 10.4%. Battery electric vehicles accounted for 17.3%, whereas hybrid electric vehicles made up  32.8% of the market. Plug-in hybrid electric vehicles (PHEV) took up 6.8% of the EU automotive market.'),
       ('journalist4', current_timestamp, 'Legalising online casinos? France places a bet on solving budget woes',
        'Lifting the ban on online casino games will help to combat illegal sites, says the government. It could also bring in much-needed tax revenue. France has put forward an amendment to legalise online casino games, a move that could boost struggling state finances. The government suggested the measure as part of the draft budget for 2025, submitted at the weekend. The text was examined by the National Assembly on Monday. If passed, virtual casino games would be taxed at 55.6% of their turnover. A study from France''s gaming watchdog (ANJ), conducted in the first three months of 2023, found gross revenue from illegal gambling sites was between €748m and €1.5bn.'),
       ('journalist4', current_timestamp,
        'COPs are struggling to keep 1.5C alive. Are there better forms of climate diplomacy?',
        'COP urgently needs to change,’ campaigners say. Here are the key reforms and alternatives that experts are proposing. Almost a decade since the Paris Agreement was signed at COP21 - with the landmark goal of limiting global heating to 1.5C - the world is hurtling towards double that. The gulf between the hope that groundbreaking deal represented and the current reality has left many people frustrated with the annual UN climate summits.  With time ticking down, some experts are questioning whether climate COPs are the best space, or structured in the best way, for the scale of action required.  “I would just like it if more countries and more parts of civil society would step back and really take a hard look at whether it will deliver what we need,” says Anthony Burke, a professor of environmental politics.'),
       ('journalist4', current_timestamp,
        'Around the world by luxury train: This 59-day adventure passes through 12 countries',
        '2025 will be the second time the experience goes ahead, suggesting there’s a real appetite for ultra high end travel by train. Have you ever wanted to travel around the world by train, but in ultimate luxury rather than in cramped couchettes? If so, you’re in luck. High-end travel agent Railbookers has just announced the return of its ‘Around the World By Luxury Train’ itinerary for 2025. Those onboard will spend 59 days riding the rails through four continents, 12 countries and over 20 cities, taking some of the most luxurious trains on the planet.'),
       ('journalist4', current_timestamp,
        'Green space in cities helps mitigate health effects of extreme heat, study finds',
        'Extreme heat is a major health concern for cities, but urban green spaces provide some relief, a new study has found. Green spaces in cities aren’t just nice to look at – they can also help protect our health, according to a new study spanning seven countries across the world. The analysis, published in the journal BMJ Open, compiled findings on heat-related health outcomes from 12 studies in Australia, Hong Kong, Portugal, Japan, South Korea, the United States, and Vietnam. It found that regions with more green space had lower rates of heat-related health problems and deaths compared with those with little greenery. The presence of trees, shrubs, and other plants in cities also appeared to boost people’s mental health, the report found, potentially offsetting the impact of high temperatures on people’s wellbeing.');


INSERT INTO comments (username, date, text, news_id)
VALUES
-- Comments for news_id 1
('user1', current_timestamp, 'It''s about time the UN engages with Russia directly.', 1),
('user2', current_timestamp, 'I hope this summit leads to productive discussions.', 1),
('user3', current_timestamp, 'Why is Guterres meeting with a country that violates international law?', 1),
('user4', current_timestamp, 'This could be a pivotal moment for BRICS relations.', 1),
('user5', current_timestamp, 'I''m skeptical about any real progress coming from this meeting.', 1),
('user6', current_timestamp, 'The world needs to see how Russia is treated by the UN.', 1),
('user7', current_timestamp, 'Guterres is walking a fine line; let''s see how he handles it.', 1),
('user8', current_timestamp, 'This summit could either escalate tensions or ease them.', 1),
('user9', current_timestamp, 'I hope human rights issues are on the agenda.', 1),
('user10', current_timestamp, 'BRICS has potential, but it needs to address global concerns.', 1),

-- Comments for news_id 2
('user1', current_timestamp, 'This sounds too good to be true! What''s the catch?', 2),
('user2', current_timestamp, 'I need to know more about this method!', 2),
('user3', current_timestamp, 'Is this legal? I’m skeptical about free access.', 2),
('user4', current_timestamp, 'This could change the game for many users.', 2),
('user5', current_timestamp, 'I hope this engineer shares some valuable insights.', 2),
('user6', current_timestamp, 'I’m curious about the ethical implications of this.', 2),
('user7', current_timestamp, 'Can’t wait to hear how this works in detail!', 2),
('user8', current_timestamp, 'This could save a lot of money for families.', 2),
('user9', current_timestamp, 'I wonder if the platforms will crack down on this.', 2),
('user10', current_timestamp, 'I’m all for saving money, but at what cost?', 2),

-- Comments for news_id 3
('user1', current_timestamp, 'Impressive resilience from Middle East Airlines.', 3),
('user2', current_timestamp, 'Safety should always come first in these situations.', 3),
('user3', current_timestamp, 'I hope passengers are aware of the risks involved.', 3),
('user4', current_timestamp, 'This is crucial for Lebanon''s economy.', 3),
('user5', current_timestamp, 'Kudos to the airline for maintaining operations.', 3),
('user6', current_timestamp, 'I wonder how they ensure safety during flights.', 3),
('user7', current_timestamp, 'Flying under conflict is a brave move.', 3),
('user8', current_timestamp, 'This could be a lifeline for many in Lebanon.', 3),
('user9', current_timestamp, 'I hope they have contingency plans in place.', 3),
('user10', current_timestamp, 'What a tough decision for the airline to make.', 3),

-- Comments for news_id 4
('user1', current_timestamp, 'This is incredibly disappointing for justice.', 4),
('user2', current_timestamp, 'How can someone escape accountability for so long?', 4),
('user3', current_timestamp, 'Victims deserve closure; this is a setback.', 4),
('user4', current_timestamp, 'Polanski''s fame seems to protect him from consequences.', 4),
('user5', current_timestamp, 'This decision raises serious questions about our legal system.', 4),
('user6', current_timestamp, 'I hope this doesn’t discourage future victims from speaking out.', 4),
('user7', current_timestamp, 'The case should have been resolved long ago.', 4),
('user8', current_timestamp, 'This sends a troubling message about sexual assault cases.', 4),
('user9', current_timestamp, 'Justice delayed is justice denied.', 4),
('user10', current_timestamp, 'We need to keep the conversation about accountability alive.', 4),

-- Comments for news_id 5
('user1', current_timestamp, 'This is a concerning prediction; we need to stay vigilant.', 5),
('user2', current_timestamp, 'I hope the US government is prepared for any unrest.', 5),
('user3', current_timestamp, 'Foreign interference in domestic affairs is alarming.', 5),
('user4', current_timestamp, 'This could escalate tensions both domestically and internationally.', 5),
('user5', current_timestamp, 'I wonder what evidence supports these claims.', 5),
('user6', current_timestamp, 'Protests can be a double-edged sword; let''s hope for peace.', 5),
('user7', current_timestamp, 'This situation needs careful monitoring.', 5),
('user8', current_timestamp, 'We must protect our democratic processes.', 5),
('user9', current_timestamp, 'Could this lead to greater international conflict?', 5),
('user10', current_timestamp, 'I hope the American people stay united despite external pressures.', 5),

-- Comments for news_id 6
('user1', current_timestamp, 'The EU needs to support green technology more actively.', 6),
('user2', current_timestamp, 'This is a critical time for the energy sector.', 6),
('user3', current_timestamp, 'I hope Brussels steps up to help these companies.', 6),
('user4', current_timestamp, 'Sales slumps can be devastating for innovation.', 6),
('user5', current_timestamp, 'Heat pumps are essential for a sustainable future.', 6),
('user6', current_timestamp, 'I wonder what specific assistance they are looking for.', 6),
('user7', current_timestamp, 'This could impact the EU''s climate goals.', 6),
('user8', current_timestamp, 'We need to invest in renewable technologies.', 6),
('user9', current_timestamp, 'Let''s hope for swift action from the EU.', 6),
('user10', current_timestamp, 'The transition to green energy is not easy; support is crucial.', 6),

-- Comments for news_id 7
('user1', current_timestamp, 'This is fascinating! Innovation is key to progress.', 7),
('user2', current_timestamp, 'I’d love to see a list of the top universities.', 7),
('user3', current_timestamp, 'Patents are vital for protecting intellectual property.', 7),
('user4', current_timestamp, 'This reflects the strength of research in Europe.', 7),
('user5', current_timestamp, 'I hope this encourages more investment in education.', 7),
('user6', current_timestamp, 'What factors contribute to higher patent applications?', 7),
('user7', current_timestamp, 'It’s interesting to see the competition among countries.', 7),
('user8', current_timestamp, 'This could lead to more collaborations in research.', 7),
('user9', current_timestamp, 'Innovation should be a priority for all nations.', 7),
('user10', current_timestamp, 'Let’s celebrate the achievements of these institutions!', 7),

-- Comments for news_id 8
('user1', current_timestamp, 'This is an important stance for artists'' rights.', 8),
('user2', current_timestamp, 'I support protecting creative works from exploitation.', 8),
('user3', current_timestamp, 'AI should not replace human creativity.', 8),
('user4', current_timestamp, 'I hope more artists join this movement.', 8),
('user5', current_timestamp, 'This raises ethical questions about AI development.', 8),
('user6', current_timestamp, 'Artists deserve compensation for their work.', 8),
('user7', current_timestamp, 'I wonder how this will impact the tech industry.', 8),
('user8', current_timestamp, 'Creativity is irreplaceable; we must defend it.', 8),
('user9', current_timestamp, 'This could set a precedent for future AI regulations.', 8),
('user10', current_timestamp, 'Let’s keep the conversation going about AI and art.', 8),

-- Comments for news_id 9
('user1', current_timestamp, 'Economic instability is always a concern for the euro.', 9),
('user2', current_timestamp, 'I hope the EU can stabilize the situation.', 9),
('user3', current_timestamp, 'Inflation and energy prices are major factors.', 9),
('user4', current_timestamp, 'This could impact trade relations across Europe.', 9),
('user5', current_timestamp, 'What measures can be taken to support the euro?', 9),
('user6', current_timestamp, 'I’m curious about the long-term implications.', 9),
('user7', current_timestamp, 'The euro''s strength is vital for the EU economy.', 9),
('user8', current_timestamp, 'Let’s hope for positive developments soon.', 9),
('user9', current_timestamp, 'This situation needs careful analysis.', 9),
('user10', current_timestamp, 'The eurozone must work together to find solutions.', 9),

-- Comments for news_id 10
('user1', current_timestamp, 'This could escalate tensions in the region.', 10),
('user2', current_timestamp, 'I hope this doesn’t lead to further violence.', 10),
('user3', current_timestamp, 'The situation in Lebanon is already fragile.', 10),
('user4', current_timestamp, 'What will be the international response to this?', 10),
('user5', current_timestamp, 'This is a significant development in the conflict.', 10),
('user6', current_timestamp, 'I wonder how Hezbollah will react to this loss.', 10),
('user7', current_timestamp, 'This raises questions about military strategies.', 10),
('user8', current_timestamp, 'Civilians often pay the highest price in conflicts.', 10),
('user9', current_timestamp, 'Let’s hope for peace in the region.', 10),
('user10', current_timestamp, 'This could have far-reaching implications for security.', 10),

-- Comments for news_id 11
('user1', current_timestamp, 'This is a serious accusation; evidence is needed.', 11),
('user2', current_timestamp, 'Trump always finds a way to shift blame.', 11),
('user3', current_timestamp, 'Foreign interference is a real issue, but is this just a distraction?', 11),
('user4', current_timestamp, 'I wonder how the Labour Party will respond to this.', 11),
('user5', current_timestamp, 'Accusations without proof can be dangerous.', 11),
('user6', current_timestamp, 'This could further strain US-UK relations.', 11),
('user7', current_timestamp, 'Is this a political tactic ahead of elections?', 11),
('user8', current_timestamp, 'We need to focus on actual foreign interference, not just claims.', 11),
('user9', current_timestamp, 'This statement could polarize opinions even more.', 11),
('user10', current_timestamp, 'Let’s hope for a fair electoral process on both sides.', 11),

-- Comments for news_id 12
('user1', current_timestamp, 'This could hurt Kazakhstan''s economy significantly.', 12),
('user2', current_timestamp, 'Is this a tactic to pressure Kazakhstan into joining?', 12),
('user3', current_timestamp, 'Trade wars are never beneficial for either side.', 12),
('user4', current_timestamp, 'I hope this doesn’t escalate into further sanctions.', 12),
('user5', current_timestamp, 'This shows how fragile regional relationships can be.', 12),
('user6', current_timestamp, 'What will be the impact on consumers in Russia?', 12),
('user7', current_timestamp, 'Kazakhstan needs to diversify its trade partners.', 12),
('user8', current_timestamp, 'This move could backfire for Russia in the long run.', 12),
('user9', current_timestamp, 'I wonder how other countries will react to this ban.', 12),
('user10', current_timestamp, 'Let''s hope diplomacy prevails over economic retaliation.', 12),

-- Comments for news_id 13
('user1', current_timestamp, 'This is a concerning topic; we need to raise awareness.', 13),
('user2', current_timestamp, 'Immunity debt sounds like a new concept; I need to learn more.', 13),
('user3', current_timestamp, 'Are we seeing the long-term effects of lockdowns?', 13),
('user4', current_timestamp, 'Children''s health should always be a priority.', 13),
('user5', current_timestamp, 'This could have serious implications for public health.', 13),
('user6', current_timestamp, 'I wonder how this will affect future vaccination policies.', 13),
('user7', current_timestamp, 'It''s crucial to understand how immunity works.', 13),
('user8', current_timestamp, 'Parents need to be informed about this issue.', 13),
('user9', current_timestamp, 'This discussion is timely and necessary.', 13),
('user10', current_timestamp, 'Let''s hope for effective solutions to protect our kids.', 13),

-- Comments for news_id 14
('user1', current_timestamp, 'This is a serious accusation; we need solid evidence.', 14),
('user2', current_timestamp, 'If true, this could escalate tensions in Europe.', 14),
('user3', current_timestamp, 'I hope this isn’t just another conspiracy theory.', 14),
('user4', current_timestamp, 'Arson is a crime that endangers lives; we need accountability.', 14),
('user5', current_timestamp, 'What are the motives behind these attacks?', 14),
('user6', current_timestamp, 'This could have significant geopolitical implications.', 14),
('user7', current_timestamp, 'We need to investigate thoroughly before jumping to conclusions.', 14),
('user8', current_timestamp, 'I wonder how European leaders will respond to this claim.', 14),
('user9', current_timestamp, 'This situation needs careful monitoring.', 14),
('user10', current_timestamp, 'Let’s hope for peace and stability in Europe.', 14),

-- Comments for news_id 15
('user1', current_timestamp, 'This could shift the balance of power in the region.', 15),
('user2', current_timestamp, 'Europe should be concerned about Russia''s growing influence.', 15),
('user3', current_timestamp, 'I wonder what strategies will come out of this meeting.', 15),
('user4', current_timestamp, 'BRICS is becoming more relevant on the global stage.', 15),
('user5', current_timestamp, 'This could lead to increased tensions with the West.', 15),
('user6', current_timestamp, 'I hope Europe is prepared for any potential fallout.', 15),
('user7', current_timestamp, 'We need to watch how this affects global markets.', 15),
('user8', current_timestamp, 'This meeting could redefine alliances.', 15),
('user9', current_timestamp, 'I’m curious about the discussions that will take place.', 15),
('user10', current_timestamp, 'Let’s hope for constructive dialogue rather than conflict.', 15),

-- Comments for news_id 16
('user1', current_timestamp, 'This is troubling news for the automotive industry.', 16),
('user2', current_timestamp, 'Is the rise of electric vehicles affecting traditional sales?', 16),
('user3', current_timestamp, 'I wonder what the long-term trends will look like.', 16),
('user4', current_timestamp, 'This could impact jobs in the sector.', 16),
('user5', current_timestamp, 'Consumers need more incentives to buy new cars.', 16),
('user6', current_timestamp, 'What are the reasons behind this decline?', 16),
('user7', current_timestamp, 'This trend could affect the EU''s economic recovery.', 16),
('user8', current_timestamp, 'I hope manufacturers adapt to changing demands.', 16),
('user9', current_timestamp, 'Let''s see how this impacts the environment.', 16),
('user10', current_timestamp, 'We need to support innovation in the automotive industry.', 16),

-- Comments for news_id 17
('user1', current_timestamp, 'This could generate much-needed revenue for France.', 17),
('user2', current_timestamp, 'I hope this doesn’t lead to increased gambling addiction.', 17),
('user3', current_timestamp, 'Is this the best solution for budget issues?', 17),
('user4', current_timestamp, 'I wonder how this will affect local casinos.', 17),
('user5', current_timestamp, 'Regulation is key to ensuring player safety.', 17),
('user6', current_timestamp, 'This could also boost tourism in France.', 17),
('user7', current_timestamp, 'Let’s see how the public reacts to this proposal.', 17),
('user8', current_timestamp, 'I hope the government takes responsible measures.', 17),
('user9', current_timestamp, 'This could set a precedent for other countries.', 17),
('user10', current_timestamp, 'Gambling can be fun, but it needs to be regulated.', 17),

-- Comments for news_id 18
('user1', current_timestamp, 'This is a critical issue that needs urgent attention.', 18),
('user2', current_timestamp, 'We need innovative solutions to combat climate change.', 18),
('user3', current_timestamp, 'Is COP the right platform for effective action?', 18),
('user4', current_timestamp, 'I wonder what alternatives could be more effective.', 18),
('user5', current_timestamp, 'Climate diplomacy is crucial for our planet''s future.', 18),
('user6', current_timestamp, 'Let’s hope for stronger commitments from all nations.', 18),
('user7', current_timestamp, 'This is a global issue that requires global cooperation.', 18),
('user8', current_timestamp, 'We need to hold leaders accountable for their promises.', 18),
('user9', current_timestamp, 'The time for action is now;
we can’t wait any longer.', 18),
('user10', current_timestamp, 'I hope we find common ground for the sake of our planet.', 18),

-- Comments for news_id 19
('user1', current_timestamp, 'This sounds like a dream vacation!', 19),
('user2', current_timestamp, 'I wonder how much this trip would cost.', 19),
('user3', current_timestamp, 'Traveling by train offers such a unique experience.', 19),
('user4', current_timestamp, 'This could be a great way to see multiple cultures.', 19),
('user5', current_timestamp, 'I hope the service is as luxurious as it sounds.', 19),
('user6', current_timestamp, 'This adventure could be a once-in-a-lifetime opportunity.', 19),
('user7', current_timestamp, 'I’d love to hear more about the itinerary!', 19),
('user8', current_timestamp, 'This is perfect for travel enthusiasts.', 19),
('user9', current_timestamp, 'What a great way to disconnect from daily life.', 19),
('user10', current_timestamp, 'Let’s hope for sustainable travel options in the future.', 19),

-- Comments for news_id 20
('user1', current_timestamp, 'This is crucial information for urban planning.', 20),
('user2', current_timestamp, 'Green spaces can improve quality of life significantly.', 20),
('user3', current_timestamp, 'I hope cities invest more in parks and greenery.', 20),
('user4', current_timestamp, 'This study highlights the importance of nature in urban areas.', 20),
('user5', current_timestamp, 'We need to prioritize green initiatives for public health.', 20),
('user6', current_timestamp, 'Let’s advocate for more sustainable city designs.', 20),
('user7', current_timestamp, 'This could help combat the urban heat island effect.', 20),
('user8', current_timestamp, 'I wonder how this will influence future city policies.', 20),
('user9', current_timestamp, 'Green spaces are essential for mental well-being.', 20),
('user10', current_timestamp, 'Let’s spread the word about the benefits of urban greenery.', 20);