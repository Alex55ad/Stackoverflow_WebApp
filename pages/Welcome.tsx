import React from 'react';
import { Title, Text, Anchor } from '@mantine/core';
import classes from './Welcome.module.css';
import { HeaderMegaMenu } from '@/components/HeaderMegaMenu/HeaderMegaMenu';
import { NavbarNested } from '@/components/NavbarNested/NavbarNested';
import { TableReviews } from '../components/TableReviews/TableReviews';

export function Welcome() {
    return (
        <>
            <Title className={classes.title} ta="center" mt={100}>
                Stack
                <Text inherit variant="gradient" component="span" gradient={{ from: 'pink', to: 'yellow' }}>
                    Underflow
                </Text>
            </Title>
            <HeaderMegaMenu />
            <Text color="dimmed" ta="center" size="lg" maw={580} mx="auto" mt="xl">
                Welcome to the StackOverflow inspired WebApp, StackUnderflow. Here, you can write questions
                and respond to other questions, helping build a community of knowledge-sharing.
            </Text>
            <Text color="dimmed" ta="center" size="lg" maw={580} mx="auto" mt="xl">
                If you're new, you can start by exploring the site and reading through existing questions
                and answers. Feel free to join discussions and contribute your insights!
            </Text>
        </>
    );
}

export default Welcome;
